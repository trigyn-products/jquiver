package com.trigyn.jws.usermanagement.security.config;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomStringUtils;

public final class CaptchaUtil {
	public static final String			FILE_TYPE	= "png";
	private static final Random			random		= new Random();
	private static final List<Color[]>	ColourSet	= new ArrayList<Color[]>();
	static String						path		= "D:/Captcha";

	enum MathOperator {
		Addition("+"), Subtraction("-");

		private String displaySign = null;

		private MathOperator(String a_displaySign) {
			displaySign = a_displaySign;
		}

		public String getDisplaySign() {
			return displaySign;
		}
	}

	static {
		ColourSet.add(new Color[] { new Color(0, 0, 50), new Color(201, 189, 255) });
		ColourSet.add(new Color[] { new Color(1, 3, 0), new Color(169, 232, 101) });
		ColourSet.add(new Color[] { new Color(0, 0, 0), new Color(246, 255, 220) });
		ColourSet.add(new Color[] { new Color(0, 0, 0), new Color(248, 252, 255) });
		ColourSet.add(new Color[] { new Color(126, 126, 126), new Color(249, 249, 249) });

		ColourSet.add(new Color[] { new Color(218, 235, 248), new Color(88, 6, 43) });
		ColourSet.add(new Color[] { new Color(255, 255, 255), new Color(0, 0, 0) });
		ColourSet.add(new Color[] { new Color(223, 215, 230), new Color(124, 79, 58)});
		ColourSet.add(new Color[] { new Color(255, 246, 255), new Color(90, 52, 139)});
		ColourSet.add(new Color[] { new Color(0, 0, 0), new Color(228, 252, 128) });

		ColourSet.add(new Color[] { new Color(172, 234, 25), new Color(1, 122, 72) });
		ColourSet.add(new Color[] { new Color(255, 255, 207), new Color(65, 101, 87)});
		ColourSet.add(new Color[] { new Color(0, 0, 0), new Color(141, 249, 137)});
		ColourSet.add(new Color[] { new Color(225, 225, 225), new Color(36, 102, 37)});
		ColourSet.add(new Color[] { new Color(254, 254, 254), new Color(78, 84, 0)});

		ColourSet.add(new Color[] { new Color(34, 1, 46), new Color(224, 79, 184) });
		ColourSet.add(new Color[] { new Color(0, 0, 30), new Color(253, 252, 134)});
		ColourSet.add(new Color[] { new Color(228, 186, 222), new Color(75, 15, 111)});
		ColourSet.add(new Color[] { new Color(225, 225, 225), new Color(152, 29, 21)});
		ColourSet.add(new Color[] { new Color(33, 0, 47), new Color(246, 79, 195)});

		ColourSet.add(new Color[] { new Color(255, 225, 225), new Color(3, 103, 101) });
		ColourSet.add(new Color[] { new Color(0, 0, 0), new Color(255, 254, 248)});
		ColourSet.add(new Color[] { new Color(255, 254, 255), new Color(19,2,0)});
		ColourSet.add(new Color[] { new Color(253, 219, 236), new Color(113, 37, 37)});
		ColourSet.add(new Color[] { new Color(225, 225, 225), new Color(105, 83, 89)});

		ColourSet.add(new Color[] { new Color(0, 11, 23), new Color(112, 165, 139) });
		ColourSet.add(new Color[] { new Color(32, 8, 8), new Color(104, 135, 243)});
		ColourSet.add(new Color[] { new Color(252, 255, 255), new Color(135, 112, 81)});
		ColourSet.add(new Color[] { new Color(4, 29, 10), new Color(180, 233, 145)});
		ColourSet.add(new Color[] { new Color(205, 233, 192), new Color(142, 108, 82)});

		ColourSet.add(new Color[] { new Color(255, 254, 255), new Color(9, 58, 75) });
		ColourSet.add(new Color[] { new Color(255, 255, 255), new Color(87, 56, 12)});
		ColourSet.add(new Color[] { new Color(0, 24, 0), new Color(241, 229, 241)});
		ColourSet.add(new Color[] { new Color(126, 28, 114), new Color(253, 253, 253)});
		ColourSet.add(new Color[] { new Color(0, 0, 4), new Color(255, 182, 209)});

		ColourSet.add(new Color[] { new Color(239, 246, 230), new Color(34, 76, 13) });
		ColourSet.add(new Color[] { new Color(254, 255, 250), new Color(42, 13, 44)});
		ColourSet.add(new Color[] { new Color(49, 63, 37), new Color(176, 219, 163)});
		ColourSet.add(new Color[] { new Color(1, 15, 0), new Color(133, 225, 250)});
		ColourSet.add(new Color[] { new Color(11, 19, 21), new Color(214, 120, 230)});
	}

	private CaptchaUtil() {
		throw new RuntimeException("This is singleton class. Please don't try to initiate this class");
	}

	// public static void main(String[] args) {
	// try {
	// String captchaStr = getCaptchaString();
	//
	// System.out.println(captchaStr);
	// for(int i = 0; i < 50; i++) {
	// //System.out.println(getCaptchaString());
	// }
	//
	// int width = 130;
	// int height = 59;
	//
	// File file = new File(path + File.separator + UUID.randomUUID().toString() +
	// ".png");
	// generateCaptcha(new Dimension(width, height), captchaStr, file);
	// } catch (Throwable a_th) {
	// a_th.printStackTrace();
	// }
	// }

	public static void generateCaptcha(Dimension size, String captchaStr, OutputStream outputStream, int captchaType) throws Exception {
		int				width			= size.width;
		int				height			= size.height;
		Color[]			colours			= getColours();

		Color			backgroundColor	= colours[0];
		Color			foregroundColor	= colours[1];

		Font			font			= new Font("Comic Sans Serif", Font.BOLD, 20);
		Font			opnFont			= new Font("Comic Sans Serif", Font.BOLD, 29);
		
		BufferedImage	cpimg			= new BufferedImage(width, height, BufferedImage.OPAQUE);
		Graphics		graphics		= cpimg.createGraphics();
		graphics.setFont(font);
		graphics.setColor(backgroundColor);
		graphics.fillRect(0, 0, width, height);
		graphics.setColor(foregroundColor);
		// graphics.drawArc(0, -10, width, height, 0, 200);
		AffineTransform	orig	= null;
		int				xPos	= 10;
		boolean			isEven	= true;
		int				charCounter	= 0;
		for (char c : captchaStr.toCharArray()) {
			orig = ((Graphics2D) graphics).getTransform();
			orig.rotate(Math.toRadians(gRPNV(20, -20)), 0, 0);
			Font rotatedFont = font.deriveFont(orig);

			if (captchaType == 1 && (charCounter == 2 || charCounter == 5)) {
				rotatedFont = opnFont.deriveFont(orig);
			}

			((Graphics2D) graphics).setFont(rotatedFont);
			((Graphics2D) graphics).drawString(c + "", xPos, 35);

			xPos	+= 20;
			isEven	= !isEven;
			charCounter++;
		}

		// ((Graphics2D) graphics).dispose();
		for (int iCounter = 12; iCounter <= 60; iCounter = iCounter + 12) {
			Graphics2D		graphics2d		= (Graphics2D) cpimg.getGraphics();
			AlphaComposite	alphaChannel	= AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
			graphics2d.setComposite(alphaChannel);
			graphics2d.setColor(foregroundColor);
			FontMetrics	fontMetrics		= graphics2d.getFontMetrics();
			String		watermarkText	= RandomStringUtils.random(20, true, true);
			Rectangle2D	rectangle		= fontMetrics.getStringBounds(watermarkText, graphics2d);

			// calculates the coordinate where the String is painted
			int			centerX			= (cpimg.getWidth() - (int) rectangle.getWidth()) / 2;
			int			centerY			= iCounter;

			graphics2d.drawString(watermarkText, centerX, centerY);
		}
		drawLines(foregroundColor, size, graphics);
		ImageIO.write(cpimg, FILE_TYPE, outputStream);
	}

	private static void drawLines(Color lineColor, Dimension size, Graphics graphics) {
		int				lineGap			= 5;
		int				horizontalCount	= (int) Math.ceil(size.height / lineGap);
		int				verticalCount	= (int) Math.ceil(size.width / lineGap);

		AlphaComposite	alphaChannel	= AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f);
		((Graphics2D) graphics).setComposite(alphaChannel);
		((Graphics2D) graphics).setStroke(new BasicStroke(0));
		graphics.setColor(lineColor);

		for (int iHorizontalCounter = 0; iHorizontalCounter < horizontalCount; iHorizontalCounter++) {
			graphics.drawLine(5, 10 * iHorizontalCounter, size.width, 10 * iHorizontalCounter);
		}

		for (int iVerticalCounter = 0; iVerticalCounter < verticalCount; iVerticalCounter++) {
			graphics.drawLine(15 * iVerticalCounter, 2, 15 * iVerticalCounter, size.height);
		}
	}

	public static int gRPNV(int max, int min) {
		// Random rand = new Random();
		int ii = min + (int) (Math.random() * ((max - (min)) + 1));
		return ii;
	}

	private static Color[] getColours() {

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = random.nextInt((ColourSet.size() - 1) + 1) + 0;
		return ColourSet.get(randomNum);
	}

	public static String getCaptchaString() {
		final String	CHARACTERS			= "abcdefghjklmnpqrstuvwxyz23456789";
		final String	CHARACTER_NUMBERS	= "23456789";

		char[]			charArray			= CHARACTERS.toCharArray();
		char[]			numberCharArray		= CHARACTER_NUMBERS.toCharArray();
		String			captchaStr			= RandomStringUtils.random(6, 0, CHARACTERS.length(), true, true, charArray).toUpperCase();

		List<Integer>	digits				= new ArrayList<Integer>();
		String			digitStr			= "";
		for (int i = 0; i < captchaStr.length(); i++) {
			if (Character.isDigit(captchaStr.charAt(i))) {
				digitStr += captchaStr.charAt(i);
			} else {
				if (!digitStr.isEmpty()) {
					digits.add(Integer.parseInt(digitStr));
					digitStr = "";
				}
			}
		}
		if (!digitStr.isEmpty()) {
			digits.add(Integer.parseInt(digitStr));
			digitStr = "";
		}

		if (digits.isEmpty()) {
			captchaStr	= captchaStr.replace(Character.toString(captchaStr.charAt(4)), RandomStringUtils.random(1, numberCharArray));
			captchaStr	= captchaStr.replace(Character.toString(captchaStr.charAt(1)), RandomStringUtils.random(1, numberCharArray));
		}

		return captchaStr;
	}

	public static int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}

	private static String[] getCharacters() {
		String[] chars = new String[36];

		for (int i = 0; i < 10; i++) {
			chars[i] = i + "";
		}
		for (int i = 10; i < 36; i++) {
			chars[i] = ((char) (i + 55)) + "";
		}
		for (int i = 36; i < 62; i++) {
			// chars[i] = ((char)(i + 61)) + "";
		}
		List<String> stringList = Arrays.asList(chars);
		Collections.shuffle(stringList);
		stringList.toArray(chars);
		return chars;
	}

	private static String generateCaptchaString(Random random) {
		int				length				= 6;
		StringBuffer	captchaStringBuffer	= new StringBuffer();
		int				ignoreCharNumbers[]	= { 48, 49, 73, 79, 105, 108, 111 };
		for (int i = 0; i < length; i++) {
			int	baseCharNumber	= Math.abs(random.nextInt()) % 62;
			int	charNumber		= 0;
			if (baseCharNumber < 26) {
				charNumber = 65 + baseCharNumber;
			} else if (baseCharNumber < 52) {
				charNumber = 97 + (baseCharNumber - 26);
			} else {
				charNumber = 48 + (baseCharNumber - 52);
			}
			int charPresent = Arrays.binarySearch(ignoreCharNumbers, charNumber);
			if (charPresent <= -1) {
				captchaStringBuffer.append((char) charNumber);
			} else {
				charNumber = charNumber + 4;
				captchaStringBuffer.append((char) charNumber);
			}
		}
		return captchaStringBuffer.toString().toUpperCase();
	}

	public static Map<String, String> getMathCaptcha() {
		Map<String, String>	captchaMap		= new HashMap<>();
		int					firstPart		= getRandomNumber(1, 10);
		int					secondPart		= getRandomNumber(1, 10);
		int					operation		= getRandomNumber(0, 2);
		
		/* Handling negative values in captcha | Mini Pillai | 04-April-2024 [Start] */
		if(operation == MathOperator.Subtraction.ordinal() && secondPart > firstPart){
		    firstPart = firstPart + secondPart;
		    secondPart = firstPart - secondPart;
		    firstPart = firstPart - secondPart;
		}
		/* Handling negative values in captcha | Mini Pillai | 04-April-2024 [End] */
		
		String				captchaString	= firstPart + MathOperator.values()[operation].getDisplaySign() + secondPart
				+ "= ";
		captchaMap.put("cs", captchaString);
		String captchaValue = "";
		switch (MathOperator.values()[operation]) {
			case Addition:
				captchaValue = firstPart + secondPart + "";
				break;
			case Subtraction:
				captchaValue = firstPart - secondPart + "";
				break;
		}

		captchaMap.put("cv", captchaValue);
		return captchaMap;
	}

}
