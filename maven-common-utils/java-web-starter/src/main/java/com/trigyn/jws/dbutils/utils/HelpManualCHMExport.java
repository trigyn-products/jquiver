package com.trigyn.jws.dbutils.utils;

import java.io.File;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dynamicform.dao.IManualEntryDetailsRepository;
import com.trigyn.jws.dynamicform.dao.IManualTypeRepository;
import com.trigyn.jws.dynamicform.entities.ManualEntryDetails;
import com.trigyn.jws.dynamicform.entities.ManualType;
import com.trigyn.jws.dynarest.entities.FileUpload;
import com.trigyn.jws.dynarest.repository.FileUploadRepository;
import com.trigyn.jws.dynarest.service.CryptoUtils;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.Parser.ParserExtension;
import com.trigyn.jws.webstarter.utils.Constant;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class HelpManualCHMExport {

	private final static Logger				logger							= LoggerFactory
			.getLogger(HelpManualCHMExport.class);

	@Autowired
	private IManualEntryDetailsRepository	iManualEntryDetailsRepository	= null;

	@Autowired
	private IManualTypeRepository			iManualTypeRepository			= null;

	@Autowired
	private FileUploadRepository			fileUploadRepository			= null;

	private final static String				JWS_SALT						= "main alag duniya";

	public void exportHelpManualToCHM(HttpServletRequest request) throws Exception {

		String						manualId		= request.getParameter("manualId");

		Optional<ManualType>		manualType		= iManualTypeRepository.findById(manualId);
		List<ManualEntryDetails>	entries			= iManualEntryDetailsRepository.findAllByManualType(manualId);

		String						downloads		= System.getProperty("user.home") + File.separator + "Downloads"
				+ File.separator;

		String						projectFolder	= downloads + manualType.get().getName() + "_chm";

		File						dir				= new File(projectFolder);
		if (dir.exists() == false)
			dir.mkdirs();

		List<ManualEntryDetails>	ordered		= buildOrderedEntries(entries);

		Map<String, String>			fileMap		= new HashMap<>();

		List<ParserExtension>		extensions	= Arrays.asList(TablesExtension.create(), AutolinkExtension.create(),
				StrikethroughExtension.create(), EmojiExtension.create());

		Parser						mdParser	= Parser.builder().extensions(extensions).build();

		HtmlRenderer				mdRenderer	= HtmlRenderer.builder().escapeHtml(false).extensions(extensions)
				.build();

		int							index		= 1;

		for (ManualEntryDetails entry : ordered) {

			String raw = entry.getEntryContent() == null ? "" : entry.getEntryContent();

			// Fix markdown formatting issues
			raw	= raw.replace("\u00A0", " ");

			// Convert **bold** into <strong>
			raw	= raw.replaceAll("\\*\\*\\s*(.*?)\\s*\\*\\*", "<strong>$1</strong>");

			// Convert __bold__ into <strong>
			raw	= raw.replaceAll("__\\s*(.*?)\\s*__", "<strong>$1</strong>");

			// Force Step X: to bold
			raw	= raw.replaceAll("(?i)\\bStep\\s*(\\d+)\\s*:", "<strong>Step $1:</strong>");

			// Remove duplicate strong tags
			raw	= raw.replaceAll("<strong>\\s*<strong>", "<strong>");
			raw	= raw.replaceAll("</strong>\\s*</strong>", "</strong>");

			// Render markdown
			String html = mdRenderer.render(mdParser.parse(raw));

			// Fix escaped HTML tags from markdown renderer
			html = html.replace("&lt;strong&gt;", "<strong>").replace("&lt;/strong&gt;", "</strong>")
					.replace("&lt;u&gt;", "<u>").replace("&lt;/u&gt;", "</u>");

			Map<String, String>	imageCache	= new HashMap<>();

			Document			parsed		= Jsoup.parse(html);

			Elements			images		= parsed.select("img");

			for (Element img : images) {

				String	src		= img.attr("src");

				String	imgName	= imageCache.get(src);

				if (imgName == null) {

					byte[] imageBytes = loadImageBytes(src);

					if (imageBytes != null) {

						imgName = "img_" + imageCache.size() + ".png";

						Path imgPath = Paths.get(projectFolder, imgName);

						Files.write(imgPath, imageBytes);

						imageCache.put(src, imgName);
					}
				}

				if (imgName != null) {
					img.attr("src", imgName);
				}
			}
			html = parsed.body().html();

			String	fileName	= "topic_" + index + ".html";

			File	htmlFile	= new File(projectFolder + File.separator + fileName);

			String	page		= "<html><head><meta charset='UTF-8'></head><body>" + "<h2>" + entry.getEntryName()
					+ "</h2>" + html + "</body></html>";

			Files.write(htmlFile.toPath(), page.getBytes(StandardCharsets.UTF_8));
			fileMap.put(entry.getManualEntryId(), fileName);

			index++;
		}
		generateTOC(projectFolder, ordered, fileMap);
		generateIndex(projectFolder, ordered, fileMap);
		generateHHP(projectFolder, ordered, fileMap, manualType.get().getName());
		compileCHM(projectFolder, manualType.get().getName());
	}

	private void generateTOC(String path, List<ManualEntryDetails> entries, Map<String, String> fileMap)
			throws Exception {

		StringBuilder toc = new StringBuilder();

		toc.append("<!DOCTYPE HTML><HTML><BODY><UL>");

		for (ManualEntryDetails e : entries) {

			toc.append("<LI><OBJECT type=\"text/sitemap\">");
			toc.append("<param name=\"Name\" value=\"" + e.getEntryName() + "\">");
			toc.append("<param name=\"Local\" value=\"" + fileMap.get(e.getManualEntryId()) + "\">");
			toc.append("</OBJECT>");
		}

		toc.append("</UL></BODY></HTML>");

		Files.write(new File(path + "/manual.hhc").toPath(), toc.toString().getBytes());
	}

	private void generateIndex(String path, List<ManualEntryDetails> entries, Map<String, String> fileMap)
			throws Exception {

		StringBuilder idx = new StringBuilder();

		idx.append("<!DOCTYPE HTML><HTML><BODY><UL>");

		for (ManualEntryDetails e : entries) {

			idx.append("<LI><OBJECT type=\"text/sitemap\">");
			idx.append("<param name=\"Name\" value=\"" + e.getEntryName() + "\">");
			idx.append("<param name=\"Local\" value=\"" + fileMap.get(e.getManualEntryId()) + "\">");
			idx.append("</OBJECT>");
		}

		idx.append("</UL></BODY></HTML>");

		Files.write(new File(path + "/manual.hhk").toPath(), idx.toString().getBytes());
	}

	private void generateHHP(String path, List<ManualEntryDetails> entries, Map<String, String> fileMap, String title)
			throws Exception {

		StringBuilder hhp = new StringBuilder();

		hhp.append("[OPTIONS]\n");
		hhp.append("Compatibility=1.1\n");
		hhp.append("Compiled file=" + title.replace(" ", "_") + ".chm\n");
		hhp.append("Contents file=manual.hhc\n");
		hhp.append("Index file=manual.hhk\n");
		hhp.append("Default topic=" + fileMap.values().iterator().next() + "\n");
		hhp.append("\n[FILES]\n");

		for (String file : fileMap.values()) {
			hhp.append(file).append("\n");
		}

		Files.write(new File(path + "/manual.hhp").toPath(), hhp.toString().getBytes());
	}

	private void compileCHM(String projectFolder, String manualName) throws Exception {

		String[]	compilerFiles	= { Constant.HHC, Constant.HHA, Constant.ITIRCL, Constant.ITSS, Constant.ITCC, Constant.HHCTRL,
				Constant.HHCOUT, Constant.HHKOUT };

		File		tempDir			= new File(System.getProperty("java.io.tmpdir"), Constant.CHCOMPILER);

		if (tempDir.exists() == false) {
			tempDir.mkdirs();
		}

		ClassLoader classLoader = getClass().getClassLoader();

		for (String fileName : compilerFiles) {

			File outFile = new File(tempDir, fileName);

			if (!outFile.exists()) {

				try (InputStream is = classLoader.getResourceAsStream("chmCompiler/" + fileName);
						FileOutputStream fos = new FileOutputStream(outFile)) {

					if (is == null) {
						throw new RuntimeException("Missing resource: chmCompiler/" + fileName);
					}

					byte[]	buffer	= new byte[8192];
					int		len;

					while ((len = is.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
					}
				}

				if (fileName.endsWith(".exe")) {
					outFile.setExecutable(true);
				}
			}
		}

		// Now run the extracted executable from temp folder
		File			exe	= new File(tempDir, "hhc.exe");

		ProcessBuilder	pb	= new ProcessBuilder(exe.getAbsolutePath(), "manual.hhp");

		pb.directory(new File(projectFolder));
		pb.redirectErrorStream(true);

		Process	process		= pb.start();

		int		exitCode	= process.waitFor();

		if (exitCode != 0 && exitCode != 1) {
			throw new RuntimeException("CHM compilation failed");
		}
	}

	/**
	 * Builds a properly ordered list of ManualEntryDetails based on: 1.
	 * Parent-child hierarchy 2. sortIndex inside each hierarchy level
	 */
	private List<ManualEntryDetails> buildOrderedEntries(List<ManualEntryDetails> entries) {

		// If input is null, return empty list
		if (entries == null) {
			return new ArrayList<>();
		}

		// Comparator to sort entries based on sortIndex
		Comparator<ManualEntryDetails>			sortByIndex	= Comparator.comparingInt(ManualEntryDetails::getSortIndex);

		// Group all entries by parentId
		Map<String, List<ManualEntryDetails>>	children	= new HashMap<>();

		for (ManualEntryDetails entry : entries) {

			// If parentId = null or blank, treat it as ROOT
			String parentId = entry.getParentId();
			if (parentId == null || parentId.trim().isEmpty()) {
				parentId = "ROOT";
			}

			// Add entry under its parent
			children.computeIfAbsent(parentId, k -> new ArrayList<>()).add(entry);
		}

		// Sort all child lists by sortIndex
		for (List<ManualEntryDetails> list : children.values()) {
			list.sort(sortByIndex);
		}

		// Depth-First Search (DFS) traversal to build ordered list, always ensures that
		// Parent appears before child and Children appear in sorted order.
		List<ManualEntryDetails>	ordered	= new ArrayList<>();

		// Recursive-like DFS using Consumer
		Consumer<String>			dfs		= new Consumer<String>() {
												@Override
												public void accept(String parentId) {

													// Get all children of this parent
													List<ManualEntryDetails> childList = children.get(parentId);

													if (childList == null) {
														return;
													}

													// Add each child + recursively process its children
													for (ManualEntryDetails child : childList) {
														ordered.add(child);
														accept(child.getManualEntryId());
													}
												}
											};

		// Start building hierarchy from ROOT
		dfs.accept("ROOT");

		// Add any orphans not included by DFS
		// (Orphans = entries whose parentId doesn't exist)
		Set<String> included = ordered.stream().map(ManualEntryDetails::getManualEntryId).collect(Collectors.toSet());

		for (ManualEntryDetails entry : entries) {
			if (!included.contains(entry.getManualEntryId())) {
				ordered.add(entry);
			}
		}

		return ordered;
	}

	/**
	 * Load image bytes from /cf/files/<id> path under UPLOAD_BASE_PATH.
	 */

	private byte[] loadImageBytes(String srcPath) {
		try {

			if (srcPath == null || srcPath.isEmpty()) {
				return null;
			}

			// Extract fileId
			String					fileId			= srcPath.substring(srcPath.lastIndexOf("/") + 1);

			Optional<FileUpload>	fileUploadOpt	= fileUploadRepository.findById(fileId);
			if (!fileUploadOpt.isPresent()) {
				logger.info("FileUpload not found in DB: " + fileId);
				return null;
			}

			FileUpload	fu				= fileUploadOpt.get();
			String		filePath		= fu.getFilePath();
			String		physicalName	= fu.getPhysicalFileName();

			File		encryptedFile	= new File(filePath, physicalName);

			// CASE 1: File under filesystem (commons/documents)
			if (filePath.contains("commons")) {

				if (!encryptedFile.exists()) {
					logger.info("Encrypted file not found: " + encryptedFile.getAbsolutePath());
					return null;
				}

				File tempDecrypted = File.createTempFile("img", ".tmp");

				CryptoUtils.decrypt(JWS_SALT, encryptedFile, tempDecrypted);

				byte[] data = Files.readAllBytes(tempDecrypted.toPath());
				tempDecrypted.delete();

				return data;
			}

			// CASE 2: Image stored inside classpath
			if (filePath.contains("images")) {

				try (InputStream encryptedStream = this.getClass().getResourceAsStream("/images/" + physicalName)) {

					if (encryptedStream == null) {
						logger.info("Classpath image NOT FOUND: /images/" + physicalName);
						return null;
					}

					File tempEncrypted = File.createTempFile("enc", ".tmp");
					Files.copy(encryptedStream, tempEncrypted.toPath(), StandardCopyOption.REPLACE_EXISTING);

					File tempDecrypted = File.createTempFile("img", ".tmp");

					CryptoUtils.decrypt(JWS_SALT, tempEncrypted, tempDecrypted);

					byte[] data = Files.readAllBytes(tempDecrypted.toPath());

					tempEncrypted.delete();
					tempDecrypted.delete();

					return data;
				}
			}

			// CASE 3: fallback filesystem path
			if (!encryptedFile.exists()) {
				logger.info("Encrypted fallback file NOT found: " + encryptedFile.getAbsolutePath());
				return null;
			}

			File tempDecrypted = File.createTempFile("img", ".tmp");

			CryptoUtils.decrypt(JWS_SALT, encryptedFile, tempDecrypted);

			byte[] data = Files.readAllBytes(tempDecrypted.toPath());

			tempDecrypted.delete();

			return data;

		} catch (Exception exc) {
			logger.info("Exception caught in loadImageBytes() :: " + exc.getMessage());
			return null;
		}
	}
}
