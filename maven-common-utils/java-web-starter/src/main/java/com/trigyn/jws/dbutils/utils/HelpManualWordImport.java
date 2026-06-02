
package com.trigyn.jws.dbutils.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFAbstractNum;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFNum;
import org.apache.poi.xwpf.usermodel.XWPFNumbering;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.freehep.graphicsio.emf.EMFHeader;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFRenderer;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynamicform.dao.IManualEntryDetailsRepository;
import com.trigyn.jws.dynamicform.dao.IManualTypeRepository;
import com.trigyn.jws.dynamicform.entities.ManualEntryDetails;
import com.trigyn.jws.dynamicform.entities.ManualType;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationRepository;
import com.trigyn.jws.webstarter.utils.Constant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 * HelpManualWordImport
 *
 * This component is responsible for importing a Microsoft Word (.docx) file and
 * converting it into: - Structured HTML content - Hierarchical manual entries
 * (Heading1 → Heading2 → Heading3) - Persisted database entities (ManualType and
 * ManualEntryDetails)
 *
 * Key responsibilities: - Parse paragraphs, lists, tables, images, diagrams
 * from Word using Apache POI - Preserve formatting (bold, italic, underline,
 * color, highlight, alignment) - Convert Word numbering and bullets into valid
 * nested HTML
 * tag ol
 * and
 * tag ul
 * - Handle custom bullets, emojis, Wingdings, picture bullets - Convert EMF
 * images to PNG for browser compatibility - Save final content into DB-backed
 * Help Manual structure
 */

@Component
public class HelpManualWordImport {

	@Autowired
	private IUserDetailsService				userDetailsService				= null;

	@Autowired
	private IManualEntryDetailsRepository	iManualEntryDetailsRepository	= null;

	@Autowired
	private IManualTypeRepository			iManualTypeRepository			= null;
	
	@Autowired
	private JwsEntityRoleAssociationRepository	jwsEntityRoleAssociationRepository;

	/**
	 * Imports a Word document and converts it into Help Manual data.
	 *
	 * Flow: 
	 * 1. Read Word file using Apache POI 
	 * 2. Create a new ManualType record 
	 * 3.Iterate through Word body elements (paragraphs and tables) 
	 * 4. Detect headings,lists, tables, images 
	 * 5. Convert everything to HTML 6. Persist structured manual entries
	 *
	 * @return generated manualId
	 */

	public String importHelpManualData(HttpServletRequest request, HttpServletResponse response, Part filePart,
			Integer sourceTypeId) throws Exception {
		InputStream iptStream = null;
		// Reads uploaded Word file safely.
		if (filePart != null) {
			iptStream = filePart.getInputStream();
		}
		try (XWPFDocument document = new XWPFDocument(iptStream)) {
			ObjectMapper	mapper			= new ObjectMapper();
			ObjectNode		root			= mapper.createObjectNode();
			ArrayNode		sections		= mapper.createArrayNode();
			UserDetailsVO	userDetailsVO	= userDetailsService.getUserDetails();
			Date			date			= new Date();
			List<String>	defaultContent	= new ArrayList<>();
			ObjectNode		currentH1		= null;
			ObjectNode		currentH2		= null;
			String			manualName		= null;
			String			manualHeader	= null;
			String 			editorName 		= null;
			int 			editorId        = 0;
			
			if (request != null && request.getParameter("manualName") != null) {
				manualName = request.getParameter("manualName");
			}
			if (request != null && request.getParameter("manualHeader") != null) {
				manualHeader = request.getParameter("manualHeader");
			}
			if (request != null && request.getParameter("editorName") != null) {
				editorName = request.getParameter("editorName");
				editorId = Integer.parseInt(editorName);
			}
			
			String		manualId	= UUID.randomUUID().toString();
			String		entityRoleId	= UUID.randomUUID().toString();
			
			// Save Help Manual Type
			ManualType	manualType	= new ManualType(manualId, manualName, 1, manualHeader, editorId , userDetailsVO.getUserName(),
					date, userDetailsVO.getUserName(), new Date());
			iManualTypeRepository.save(manualType);
			
			//Save Role as Admin for Manual Type that is imported
			JwsEntityRoleAssociation role = new JwsEntityRoleAssociation();
			role.setEntityId(manualId);
			role.setEntityName(manualName);
			role.setEntityRoleId(entityRoleId);
			role.setIsActive(1);
			role.setIsCustomUpdated(1);
			role.setLastUpdatedBy(userDetailsVO.getUserName());
			role.setLastUpdatedDate(new Date());
			role.setModuleId(Constant.HELPMANUALMODID);
			role.setModuleTypeId(0);
			role.setRoleId(Constant.ADMINROLEID);
			role.setRoleTypeId(2);
			jwsEntityRoleAssociationRepository.save(role);
			
			List<ManualEntryDetails>	manualEntryDetailsList	= new ArrayList<>();
			int							count					= 0;
			Map<String, String>			manualEntryIdMap		= new HashMap<>();

			List<IBodyElement>			bodyElementLists		= document.getBodyElements();

			Long						currentNumId			= null;
			// Track numbering counters per numId + level
			Map<Long, int[]>			numCounters				= new HashMap<>();
			int							currentIlvl				= -1;
			// This holds the HTML for open list tags that need to be closed
			StringBuilder				openListTags			= new StringBuilder();

			Long						lastLogicalNumId		= null;
			String						lastNumFmt				= null;

			for (IBodyElement bodyElement : bodyElementLists) {
				String contentHtml = "";

				if (bodyElement instanceof XWPFParagraph) {
					XWPFParagraph para = (XWPFParagraph) bodyElement;
					if (para != null) {
						String text = para.getText().trim();
						// Skip empty paragraphs.Avoids polluting HTML with empty <p> tags.
						if (text.isEmpty() && para.getRuns().isEmpty() && !hasDrawing(para))
							continue;
						String style = para.getStyle();
						if (style == null && para.getStyleID() != null) {
							style = para.getStyleID();
						}

						// --- POI List Level Check ---
						// Word list identity
						Long	numId	= para.getNumID() != null ? para.getNumID().longValue() : null;
						String	numFmt	= getNumFormat(para);											// decimal,bullet,roman, letter etc.
						if (numId != null) {
							// Correctly handles nested lists, List Continuation across paragraphs, Level
							// changes(indent/outdent)
							int		ilvl		= getListLevel(para, document);
							boolean	isOrdered	= numFmt != null && (numFmt.equalsIgnoreCase("decimal")
									|| numFmt.toLowerCase().contains("letter") || numFmt.toLowerCase().contains("roman")
									|| numFmt.equalsIgnoreCase("upperRoman"));

							// ---- LIST TRANSITIONS (unchanged) ----
							if (currentNumId != null && numId.equals(currentNumId)) {
								// close only the previous <li>
								contentHtml += "</li>";
							}
							boolean sameLogicalList = lastLogicalNumId != null && ilvl == currentIlvl && numFmt != null
									&& numFmt.equalsIgnoreCase(lastNumFmt);

							// START NEW LIST ONLY IF LOGICALLY DIFFERENT
							if (currentNumId == null) {
								openNewLists(openListTags, -1, ilvl, isOrdered, para);
							} else if (sameLogicalList == false && numId.equals(currentNumId) == false) {
								closeOpenLists(openListTags, currentIlvl, -1);
								openNewLists(openListTags, -1, ilvl, isOrdered, para);
							} else if (ilvl > currentIlvl) {
								openNewLists(openListTags, currentIlvl, ilvl, isOrdered, para);
							} else if (ilvl < currentIlvl) {
								closeOpenLists(openListTags, currentIlvl, ilvl);
							}
							// Emit opening tags that were queued
							contentHtml += openListTags.toString();
							openListTags.setLength(0);
							// ---- COUNTER HANDLING ----
							int[] counters = numCounters.computeIfAbsent(numId, k -> new int[9]);

							// reset deeper levels
							for (int i = ilvl + 1; i < counters.length; i++) {
								counters[i] = 0;
							}
							// increment current level
							counters[ilvl]++;

							// calculate list value (HTML needs absolute position)
							int				liValue	= counters[ilvl];

							// paragraph-level alignment and indentation to the list item
							StringBuilder	liStyle	= new StringBuilder("margin-bottom: 5px;");
							if (para.getIndentationLeft() > 0) {
								liStyle.append("margin-left:").append(para.getIndentationLeft() / 30).append("px;");
							}
							contentHtml			+= "<li value='" + liValue + "' style='" + liStyle.toString() + "'>";
							contentHtml			+= extractRunContent(para);
							lastLogicalNumId	= numId;
							lastNumFmt			= numFmt;
							currentNumId		= numId;
							currentIlvl			= ilvl;
							// For handling custom bullets
							if (isCustomBullet(numId) == true) {
								contentHtml = "<li>" + "<span class='bullet'>" + bulletByNumId(numId) + "</span> "
										+ extractRunContent(para);
							}

						} else {
							if (currentNumId != null) {
								contentHtml += "</li>";
								closeOpenLists(openListTags, currentIlvl, -1);
								contentHtml += openListTags.toString();
								openListTags.setLength(0);

								numCounters.remove(currentNumId);
								lastLogicalNumId	= null;
								lastNumFmt			= null;

								currentNumId		= null;
								currentIlvl			= -1;
								contentHtml			+= extractParagraphWithImages(para);
							} else {
								// For processing diagrams
								if (hasDrawing(para)) {
									contentHtml += extractParagraphWithImages(para);
									// For processing text with diagrams
								} else if (hasDrawing(para) == false) {
									if (numId == null && text != "") {
										contentHtml += extractParagraphWithImages(para);
									}
								}
							}
						}

						if ("Heading1".equalsIgnoreCase(style)) {
							currentH1	= mapper.createObjectNode();
							count		= processHeading(style, currentH1, text, count, userDetailsVO, manualId, date,
									mapper, manualEntryIdMap, manualEntryDetailsList);
							sections.add(currentH1);
							currentH2 = null;

						} else if ("Heading2".equalsIgnoreCase(style)) {
							if (currentH1 == null)
								continue;
							currentH2 = mapper.createObjectNode();
							((ArrayNode) currentH1.get("subsections")).add(currentH2);
							count = processHeading(style, currentH1, text, count, userDetailsVO, manualId, date, mapper,
									manualEntryIdMap, manualEntryDetailsList);

						} else if ("Heading3".equalsIgnoreCase(style)) {

							if (currentH2 == null)
								continue;
							ObjectNode h3 = mapper.createObjectNode();
							h3.put("heading", text);
							h3.put("content", "");
							count = processHeading(style, currentH1, text, count, userDetailsVO, manualId, date, mapper,
									manualEntryIdMap, manualEntryDetailsList);
						} else {

							if (manualEntryDetailsList.isEmpty() == false) {
								ManualEntryDetails manualEntryDetails = manualEntryDetailsList
										.get(manualEntryDetailsList.size() - 1);
								manualEntryDetails.setEntryContent(manualEntryDetails.getEntryContent() + contentHtml);
								manualEntryDetailsList.set(manualEntryDetailsList.size() - 1, manualEntryDetails);
							} else {
								defaultContent.add(contentHtml);
							}
						}
					}
				} else if (bodyElement instanceof XWPFTable) {

					// Close any open list structures before inserting a table
					if (currentNumId != null) {
						String closureHtml = "</li>"; // Close last <li>
						closeOpenLists(openListTags, currentIlvl, -1); // Close UL/OL tags
						closureHtml += openListTags.toString();

						// Append closure to the last manual entry
						if (manualEntryDetailsList.isEmpty() == false) {
							ManualEntryDetails manualEntryDetails = manualEntryDetailsList
									.get(manualEntryDetailsList.size() - 1);
							manualEntryDetails.setEntryContent(manualEntryDetails.getEntryContent() + closureHtml);
							manualEntryDetailsList.set(manualEntryDetailsList.size() - 1, manualEntryDetails);
						} else {
							defaultContent.add(closureHtml);
						}

						openListTags.setLength(0);
						currentNumId	= null;
						currentIlvl		= -1;
					}

					XWPFTable		table		= (XWPFTable) bodyElement;
					StringBuilder	tableHtml	= new StringBuilder();

					// Extract width if possible
					CTTblPr			tblPr		= table.getCTTbl().getTblPr();
					String			tableStyle	= "";

					if (tblPr != null && tblPr.getTblW() != null && tblPr.getTblW().getW() != null) {
						BigInteger	width		= (BigInteger) tblPr.getTblW().getW();
						String		widthType	= tblPr.getTblW().getType().toString();
						if ("pct".equalsIgnoreCase(widthType)) {
							tableStyle += "width:" + (width.intValue() / 50) + "%;";
						} else {
							tableStyle += "width:" + width.intValue() + "px;";
						}
					}
					tableHtml.append("<table style='" + tableStyle + "border-collapse:collapse;'>");

					for (XWPFTableRow row : table.getRows()) {
						tableHtml.append("<tr>");
						for (XWPFTableCell cell : row.getTableCells()) {
							StringBuilder tdStyle = new StringBuilder();

							if (cell.getColor() != null) {
								tdStyle.append("background-color:#").append(cell.getColor()).append(";");
							}

							if (cell.getVerticalAlignment() != null) {
								tdStyle.append("vertical-align:")
										.append(cell.getVerticalAlignment().name().toLowerCase()).append(";");
							}

							tdStyle.append("padding:6px;");

							tableHtml.append("<td style='" + tdStyle + "'>");

							for (IBodyElement cellElem : cell.getBodyElements()) {
								if (cellElem instanceof XWPFParagraph) {
									// For cell content, use the P-wrapped version
									tableHtml.append(extractParagraphWithImages((XWPFParagraph) cellElem));
								} else if (cellElem instanceof XWPFTable) {
									// Optional: recursive table rendering
									tableHtml.append("<div>[Nested table]</div>");
								}
							}

							tableHtml.append("</td>");
						}
						tableHtml.append("</tr>");
					}
					tableHtml.append("</table>");

					if (manualEntryDetailsList.isEmpty() == false) {
						ManualEntryDetails	manualEntryDetails	= manualEntryDetailsList
								.get(manualEntryDetailsList.size() - 1);
						String				tblHTML				= sanitizeHtml(tableHtml.toString());
						manualEntryDetails.setEntryContent(manualEntryDetails.getEntryContent() + tblHTML.toString());
						manualEntryDetailsList.set(manualEntryDetailsList.size() - 1, manualEntryDetails);
					} else {
						defaultContent.add(tableHtml.toString());
					}
				}

			} // for

			if (currentNumId != null) {
				String finalClosure = "</li>";
				// close all open lists (pop stack appropriately)
				closeOpenLists(openListTags, currentIlvl, -1);
				finalClosure += openListTags.toString();
				// listStackHolder is cleared to avoid leaking state across imports
				listStackHolder.clear();

				if (manualEntryDetailsList.isEmpty() == false) {
					ManualEntryDetails manualEntryDetails = manualEntryDetailsList
							.get(manualEntryDetailsList.size() - 1);
					manualEntryDetails.setEntryContent(manualEntryDetails.getEntryContent() + finalClosure);
					manualEntryDetailsList.set(manualEntryDetailsList.size() - 1, manualEntryDetails);
				} else {
					defaultContent.add(finalClosure);
				}
			}

			// Save to DB
			if (manualEntryDetailsList.isEmpty() == false) {
				iManualEntryDetailsRepository.saveAll(manualEntryDetailsList);
			}
			if (defaultContent.isEmpty() == false) {
				root.put("defaultContent", String.join("\n", defaultContent));
			}
			root.set("sections", sections);

			return manualId;
		}
	}

	private int processHeading(String style, ObjectNode currentHeader, String text, int count,
			UserDetailsVO userDetailsVO, String manualId, Date date, ObjectMapper mapper,
			Map<String, String> manualEntryIdMap, List<ManualEntryDetails> manualEntryDetailsList) {

		ManualEntryDetails	med				= new ManualEntryDetails();
		String				manualEntryId	= UUID.randomUUID().toString();

		med.setManualEntryId(manualEntryId);
		med.setEntryName(text);
		med.setEntryContent("");
		med.setSortIndex(count);
		med.setAction("add");
		med.setCreatedBy(userDetailsVO.getUserName());
		med.setLastUpdatedBy(userDetailsVO.getUserName());
		med.setCreatedDate(date);
		med.setLastModifiedOn(date);
		med.setManualId(manualId);

		if (style.equalsIgnoreCase("Heading1")) {

			med.setParentId(null);
			manualEntryIdMap.put("H1", manualEntryId);
			manualEntryDetailsList.add(med);

			currentHeader.put("heading", text);

			// ALWAYS CREATE SUBSECTIONS
			currentHeader.set("subsections", mapper.createArrayNode());

		} else if (style.equalsIgnoreCase("Heading2")) {

			String parentId = manualEntryIdMap.get("H1");
			med.setParentId(parentId);
			manualEntryIdMap.put("H2", manualEntryId);
			manualEntryDetailsList.add(med);

			currentHeader.put("heading", text);

			// ALWAYS CREATE SUBSECTIONS
			currentHeader.set("subsections", mapper.createArrayNode());

		} else if (style.equalsIgnoreCase("Heading3")) {

			String parentId = manualEntryIdMap.get("H2");
			med.setParentId(parentId);
			manualEntryIdMap.put("H3", manualEntryId);
			manualEntryDetailsList.add(med);

			currentHeader.put("heading", text);

			// H3 HAS NO CHILD HEADINGS, SO HAS CONTENT FIELD
			currentHeader.put("content", "");
		}

		return count + 1;
	}

	/**
	 * Maps Word internal numbering IDs to Unicode bullet symbols.
	 *
	 * Word often uses hidden picture bullets or Wingdings. These are converted into
	 * visually similar Unicode symbols.
	 */

	private static String bulletByNumId(Long numId) {
		if (numId == null)
			return "●";

		switch (numId.intValue()) {
			case 12:
			case 10:
				return "●"; // plus bullet
			case 86:
				return "■";
			case 87:
				return "❖"; // diamond
			case 30:
			case 71:
				return "➤"; // arrow
			case 28:
				return "⊞"; // Word picture-bullet replacement
			case 45:
			case 31:
				return "✓"; // check bullet
			case 55:
			case 32:
				return "■"; // square bullet
			case 26:
				return "I"; // upperRoman
			default:
				return "●";
		}
	}

	private static boolean hasDrawing(XWPFParagraph para) {
		for (XWPFRun run : para.getRuns()) {
			if (run == null)
				continue;
			if (run.getCTR() != null && run.getCTR().sizeOfDrawingArray() > 0) {
				return true;
			}
		}
		return false;
	}

	private static Integer getParagraphListLevel(XWPFParagraph para) {
		if (para.getCTP().getPPr() != null && para.getCTP().getPPr().getNumPr() != null
				&& para.getCTP().getPPr().getNumPr().getIlvl() != null) {

			return para.getCTP().getPPr().getNumPr().getIlvl().getVal().intValue();
		}
		return -1;
	}

	private int getListLevel(XWPFParagraph para, XWPFDocument document) {
		if (para.getNumID() == null)
			return -1;

		BigInteger		numId		= para.getNumID();
		XWPFNumbering	numbering	= document.getNumbering();
		XWPFNum			num			= numbering.getNum(numId);

		if (num == null)
			return -1;

		BigInteger		abstractNumId	= num.getCTNum().getAbstractNumId().getVal();
		XWPFAbstractNum	abs				= numbering.getAbstractNum(abstractNumId);

		// Paragraph may NOT contain ilvl ⇒ fallback to abstract def
		if (para.getNumIlvl() != null) {
			return para.getNumIlvl().intValue();
		}

		// fallback: read from lvl definitions
		for (CTLvl lvl : abs.getAbstractNum().getLvlList()) {
			// detect actual lvl by matching lvl attributes
			return lvl.getIlvl().intValue(); // fallback
		}

		return 0;
	}

	private static String getNumFormat(XWPFParagraph para) {

		if (para.getNumID() == null)
			return null;

		XWPFNumbering numbering = para.getDocument().getNumbering();
		if (numbering == null)
			return null;

		XWPFNum num = numbering.getNum(para.getNumID());
		if (num == null)
			return null;

		XWPFAbstractNum abs = numbering.getAbstractNum(num.getCTNum().getAbstractNumId().getVal());
		if (abs == null)
			return null;

		int		ilvl	= getParagraphListLevel(para);
		CTLvl	lvl		= abs.getAbstractNum().getLvlArray(ilvl);

		if (lvl == null || lvl.getNumFmt() == null)
			return null;

		return lvl.getNumFmt().getVal().toString();
	}

	// --- New Helper Methods for List Closure/Opening ---

	/**
	 * Helper to close open list tags when moving up a level or ending a list.
	 * Prevents broken HTML like:
	 * <ul>
	 * <li>Item
	 * <p>
	 * Next paragraph
	 * 
	 * @param openListTags The StringBuilder holding the current closure tags.
	 * @param currentIlvl  The current indentation level.
	 * @param targetIlvl   The level to stop closing at (i.e., the new current
	 *                     level, use -1 to close all).
	 */
	private void closeOpenLists(StringBuilder openListTags, int currentIlvl, int targetIlvl) {
		int levelsToClose = currentIlvl - targetIlvl;
		if (levelsToClose < 0) {
			levelsToClose = 0;
		}
		for (int i = 0; i < levelsToClose; i++) {
			// pop a tag type from listStack and close the corresponding tag
			String popped = listStackPop();
			if ("ol".equalsIgnoreCase(popped)) {
				openListTags.append("</ol>");
			} else {
				// default to ul if unknown
				openListTags.append("</ul>");
			}
		}
	}

	/**
	 * Helper to open new list tags when moving down a level or starting a new list.
	 * Opens required
	 * <ol>
	 * /
	 * <ul>
	 * tags when entering deeper list levels.
	 * 
	 * @param openListTags The StringBuilder that will accumulate the new open list
	 *                     tags.
	 * @param startIlvl    The level to start opening from (exclusive).
	 * @param endIlvl      The level to stop opening at (inclusive).
	 */

	private void openNewLists(StringBuilder openListTags, int startIlvl, int endIlvl, boolean isOrdered,
			XWPFParagraph para) {
		for (int i = startIlvl + 1; i <= endIlvl; i++) {

			if (isOrdered) {
				boolean	hasIcon		= hasIconOrPictureBullet(para);
				String	listType	= getOrderedListType(para);
				// Open <ol> with correct type
				openListTags.append("<ol");
				if (listType != null) {
					openListTags.append(" type='").append(listType).append("'");
				}
				openListTags.append(">");
				listStackAdd(openListTags, "ol");
				if (hasIcon) {
					// ✅ bullets / icons / emoji
					openListTags.append("<ul>");
				}

			} else {
				// ✅ bullets / icons / emoji
				openListTags.append("<ul>");
				listStackAdd(openListTags, "ul");
			}
		}
	}

	private boolean isCustomBullet(Long numId) {
		return numId != null
				&& (numId == 10 || numId == 86 || numId == 9 || numId == 12 || numId == 28 || numId == 45 || numId == 55
						|| numId == 30 || numId == 31 || numId == 32 || numId == 26 || numId == 71 || numId == 87);
	}

	/**
	 * Returns '1' for decimal, 'a' for lowerLetter, 'A' for upperLetter, 'i' for
	 * lowerRoman, 'I' for upperRoman
	 */
	private String getOrderedListType(XWPFParagraph para) {

		if (para.getNumID() == null)
			return null;

		XWPFNumbering numbering = para.getDocument().getNumbering();
		if (numbering == null)
			return null;

		XWPFNum num = numbering.getNum(para.getNumID());
		if (num == null)
			return null;

		XWPFAbstractNum abs = numbering.getAbstractNum(num.getCTNum().getAbstractNumId().getVal());
		if (abs == null)
			return null;

		int		ilvl	= getParagraphListLevel(para);
		CTLvl	lvl		= abs.getAbstractNum().getLvlArray(ilvl);
		if (lvl == null)
			return null;

		String numFmt = lvl.getNumFmt() != null ? lvl.getNumFmt().getVal().toString() : null;

		if (numFmt == null)
			return null;

		switch (numFmt.toLowerCase()) {
			case "decimal":
				return "1";
			case "lowerletter":
				return "a";
			case "upperletter":
				return "A";
			case "lowerroman":
				return "i";
			case "upperroman":
				return "I";
			case "upperRoman":
				return "I";
			default:
				return null;
		}
	}

	private boolean hasIconOrPictureBullet(XWPFParagraph para) {

		for (XWPFRun run : para.getRuns()) {

			// 1️⃣ Embedded pictures (PNG/JPG/EMF bullets)
			if (!run.getEmbeddedPictures().isEmpty()) {
				return true;
			}

			// 2️⃣ Emoji or symbol bullets (Unicode)
			String text = run.getText(0);
			if (text != null && containsEmojiOrSymbol(text)) {
				return true;
			}

			// 3️⃣ Word drawing / pict bullets
			CTR ctr = run.getCTR();
			if (ctr != null) {
				if (!ctr.getPictList().isEmpty())
					return true;
				if (!ctr.getDrawingList().isEmpty())
					return true;
			}
		}
		return false;
	}

	private boolean containsEmojiOrSymbol(String text) {

		for (int i = 0; i < text.length(); i++) {
			int codePoint = text.codePointAt(i);

			// Emoji ranges
			if ((codePoint >= 0x1F300 && codePoint <= 0x1FAFF) || // emojis
					(codePoint >= 0x2600 && codePoint <= 0x27BF)) { // symbols
				return true;
			}
		}
		return false;
	}

	// Helper: push a list type into the stack Word lists are stateful
	// HTML lists must be explicitly closed
	// This stack tracks <ol> / <ul> so nesting closes correctly

	private final List<String> listStackHolder = new ArrayList<>();

	private void listStackAdd(StringBuilder ignored, String type) {
		listStackHolder.add(type);
	}

	private String listStackPop() {
		if (listStackHolder.isEmpty())
			return "ul";
		return listStackHolder.remove(listStackHolder.size() - 1);
	}

	/**
	 * Cleans up generated HTML for responsiveness.
	 *
	 * Fixes: - Very large widths (9710px) - Tables exceeding screen width - Images
	 * not scaling
	 */
	public static String sanitizeHtml(String html) {
		if (html == null || html.isEmpty()) {
			return html;
		}

		// 1. Replace extremely large widths like width:9710px with width:100%
		html = html.replaceAll("(?i)width\\s*:\\s*(\\d{4,})px", "width:100%");

		// 2. Fix <table> tags to have safe max-width
		Pattern			tablePattern	= Pattern.compile("(?i)<table([^>]*)style=[\"']([^\"']*)[\"']");
		Matcher			tableMatcher	= tablePattern.matcher(html);
		StringBuffer	tableResult		= new StringBuffer();
		while (tableMatcher.find()) {
			String	attrs	= tableMatcher.group(1);
			String	style	= tableMatcher.group(2);

			// Remove large width
			style = style.replaceAll("width\\s*:\\s*\\d+px\\s*;?", "");

			// Add max-width & auto width
			if (style.toLowerCase().contains("max-width") == false) {
				style += "max-width:100%; width:auto;";
			}

			if (style.toLowerCase().contains("margin:auto") == false) {
				style += " margin:auto;";
			}
			String replacement = "<table" + attrs + " style=\"" + style.trim() + "\"";
			tableMatcher.appendReplacement(tableResult, Matcher.quoteReplacement(replacement));
		}
		tableMatcher.appendTail(tableResult);
		html = tableResult.toString();

		// 3. Fix <img> tags to have max-width
		Pattern			imgPattern	= Pattern.compile("(?i)<img([^>]*)style=[\"']([^\"']*)[\"']");
		Matcher			imgMatcher	= imgPattern.matcher(html);
		StringBuffer	imgResult	= new StringBuffer();
		while (imgMatcher.find()) {
			String	attrs	= imgMatcher.group(1);
			String	style	= imgMatcher.group(2);

			// Remove fixed width
			style = style.replaceAll("width\\s*:\\s*\\d+px\\s*;?", "");

			// Add responsive styles
			if (style.toLowerCase().contains("max-width") == false) {
				style += "; max-width:100%; height:auto;";
			}

			String replacement = "<img" + attrs + " style=\"" + style.trim() + "\"";
			imgMatcher.appendReplacement(imgResult, Matcher.quoteReplacement(replacement));
		}
		imgMatcher.appendTail(imgResult);
		html = imgResult.toString();

		return html;
	}

	/**
	 * Converts a paragraph into
	 * <p>
	 * HTML.
	 *
	 * Preserves: - Alignment (left, center, justify) - Indentation - Embedded
	 * images
	 */

	private String extractParagraphWithImages(XWPFParagraph para) throws IOException {
		StringBuilder		style		= new StringBuilder();

		// ---- Paragraph Alignment ----
		ParagraphAlignment	alignment	= para.getAlignment();
		if (alignment != null) {
			if (alignment == ParagraphAlignment.BOTH || alignment == ParagraphAlignment.DISTRIBUTE) {
				style.append("text-align:justify;");
			} else {
				style.append("text-align:").append(alignment.name().toLowerCase()).append(";");
			}
		}

		// ---- Indentation left ----
		if (para.getIndentationLeft() > 0) {
			// Convert Twips to pixels (approx 1/20 of a point)
			int indentPx = para.getIndentationLeft() / 20;
			style.append("margin-left:").append(indentPx).append("px;");
		}

		// Add display block to ensure it respects the alignment
		style.append("display:block;");

		return "<p style=\"" + style.toString() + "\">" + extractRunContent(para) + "</p>";
	}

	/**
	 * Extracts content from runs (text, formatting, images) without any paragraph
	 * or list wrappers. Used for content inside
	 * <p>
	 * or
	 * <li>tags. Converts Word runs into inline HTML. Preserves: - Bold, Italic,
	 * Underline - Font size, family, color - Background highlights - Embedded
	 * images (PNG/JPG/EMF)
	 */

	private String extractRunContent(XWPFParagraph para) throws IOException {

		StringBuilder sb = new StringBuilder();

		for (XWPFRun run : para.getRuns()) {
			if (run == null || run.text() == null)
				continue;

			String	text	= run.text().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");

			String	rawText	= run.toString();
			// Detect Wingdings/Symbol bullets
			if (rawText != null && rawText.length() == 1) {

				char	c		= rawText.charAt(0);
				String	mapped	= mapSymbolBullet(c);

				if (mapped != null) {
					sb.append("<span class='custom-bullet'>" + mapped + "</span>");
					continue; // Skip normal text render
				}
			}

			String			color		= run.getColor();
			int				fontSize	= run.getFontSize();
			String			fontFamily	= run.getFontFamily();

			StringBuilder	style		= new StringBuilder();
			// Text color
			style.append("color:").append(color != null ? "#" + color : "#000000").append(";");

			// Font size
			if (fontSize > 0) {
				style.append("font-size:").append(fontSize).append("pt;");
			}

			// Font family
			if (fontFamily != null) {
				style.append("font-family:'").append(fontFamily).append("';");
			}

			// Highlight background (purple, yellow, blue, etc.)
			CTRPr rPr = run.getCTR().getRPr();

			if (rPr != null && rPr.sizeOfHighlightArray() > 0) {

				CTHighlight	ctHighlight		= rPr.getHighlightArray(0);
				String		highlightValue	= ctHighlight.getVal().toString();

				String		cssColor		= convertHighlightToCss(highlightValue);

				style.append("background-color:").append(cssColor).append(";");
			}

			StringBuilder runHtml = new StringBuilder();
			runHtml.append("<span style=\"").append(style).append("\">");

			if (run.isBold())
				runHtml.append("<strong>");
			if (run.isItalic())
				runHtml.append("<em>");
			if (run.getUnderline() != UnderlinePatterns.NONE)
				runHtml.append("<u>");

			runHtml.append(text);

			if (run.getUnderline() != UnderlinePatterns.NONE)
				runHtml.append("</u>");
			if (run.isItalic())
				runHtml.append("</em>");
			if (run.isBold())
				runHtml.append("</strong>");

			runHtml.append("</span>");
			sb.append(runHtml);

			// Process embedded images
			for (XWPFPicture pic : run.getEmbeddedPictures()) {
				XWPFPictureData	picData		= pic.getPictureData();
				String			extension	= picData.suggestFileExtension();
				String			mime		= getImageMimeType(extension);

				byte[]			imageBytes	= picData.getData();

				if (mime.equals("unsupported")) {
					if ("emf".equals(extension)) {
						// Convert EMF to PNG
						File	tempEmf	= File.createTempFile("image", ".emf");
						File	tempPng	= File.createTempFile("image", ".png");

						try (FileOutputStream fos = new FileOutputStream(tempEmf)) {
							fos.write(imageBytes);
						}

						convertEmfToPng(tempEmf, tempPng);

						byte[]	pngBytes	= Files.readAllBytes(tempPng.toPath());
						String	base64Png	= Base64.getEncoder().encodeToString(pngBytes);
						sb.append("<table style='" + "width:100%;max-width:100%; width:auto margin:auto;"
								+ "border-collapse:collapse;'>").append("<tr>")
								.append("<td style='padding:6px; text-align:center;'>")
								.append("<img src='data:image/png;base64,").append(base64Png)
								.append("' style='max-width:100%; display:block; margin:auto;' />").append("</td>")
								.append("</tr>").append("</table>");

						// Clean up temp files
						tempEmf.delete();
						tempPng.delete();
					} else {
						sb.append("<div style='color:red;font-style:italic'>[Unsupported image type:").append(extension)
								.append("]</div>");
					}
					continue;
				}
				String base64 = Base64.getEncoder().encodeToString(picData.getData());
				sb.append("<img src=\"data:").append(mime).append(";base64,").append(base64)
						.append("\" style=\"max-width:100%;\" />");
			}
		}
		return sb.toString();
	}

	/**
	 * Converts Wingdings / Symbol font bullets into visible Unicode equivalents.
	 */

	private static String mapSymbolBullet(char c) {
		int code = (int) c;

		switch (code) {

			case 0xF0CA:
				return "❖"; // diamond | Wingdings
			case 0xF0A7:
				return "❯"; // arrow
			case 0xF0B7:
				return "•"; // dot
			case 0xF0FC:
				return "◆"; // filled diamond
			case 0xF0D8:
				return "◈"; // diamond outline

			// Symbols common in Word
			case 0x2022:
				return "•"; // normal bullet
			case 0x25E6:
				return "◦"; // small ring
			case 0x25AA:
				return "▪"; // small square
			case 0x25CB:
				return "○"; // large ring

			default:
				return null;
		}
	}

	private static String convertHighlightToCss(String highlight) {
		if (highlight == null)
			return "transparent";

		switch (highlight.toLowerCase()) {
			case "yellow":
				return "#ffff00";
			case "green":
				return "#00ff00";
			case "cyan":
			case "turquoise":
				return "#40e0d0";
			case "blue":
				return "#0000ff";
			case "red":
				return "#ff0000";
			case "pink":
				return "#ff00ff";
			case "darkblue":
				return "#00008b";
			case "darkcyan":
				return "#008b8b";
			case "darkgreen":
				return "#006400";
			case "darkmagenta":
				return "#8b008b";
			case "darkred":
				return "#8b0000";
			case "darkyellow":
				return "#9b870c";
			case "gray50":
				return "#7f7f7f";
			case "gray25":
				return "#c0c0c0";
			case "black":
				return "#000000";
			case "purple":
			case "violet":
			case "magenta2":
				return "#800080";

			default:
				return "transparent";
		}
	}

	public static void convertEmfToPng(File emfFile, File pngFile) throws IOException {
		try (FileInputStream fis = new FileInputStream(emfFile);
				EMFInputStream emfIn = new EMFInputStream(fis, EMFInputStream.DEFAULT_VERSION)) {

			// Get EMF header for size
			EMFHeader	header	= emfIn.readHeader();
			int			width	= (int) header.getBounds().getWidth();
			int			height	= (int) header.getBounds().getHeight();

			if (width <= 0 || height <= 0) {
				width	= 800;
				height	= 600;
			}

			BufferedImage	image	= new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D		g2		= image.createGraphics();

			// Optional: High quality rendering
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

			// Paint EMF content
			EMFRenderer renderer = new EMFRenderer(emfIn);
			renderer.paint(g2);

			g2.dispose();
			ImageIO.write(image, "png", pngFile);
		}
	}

	private static String getImageMimeType(String extension) {
		switch (extension.toLowerCase()) {
			case "png":
				return "image/png";
			case "jpg":
			case "jpeg":
				return "image/jpeg";
			case "gif":
				return "image/gif";
			case "bmp":
				return "image/bmp";
			case "svg":
				return "image/svg+xml"; // Supported by modern browsers
			case "tiff":
			case "tif":
				return "image/tiff";
			case "emf":
			case "wmf":
				return "unsupported"; // Not supported in <img> base64 in HTML
			default:
				return "unsupported";
		}
	}

}