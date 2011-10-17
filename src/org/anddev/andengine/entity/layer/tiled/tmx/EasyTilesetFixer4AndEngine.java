package org.anddev.andengine.entity.layer.tiled.tmx;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author Nicolas Gramlich
 * @since 20:39:47 - 28.07.2010
 * @Extender Luiz Carvalho
 * @in Oct 2011
 */
public class EasyTilesetFixer4AndEngine {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public static void main(final String[] args) throws ParseException {
		final Options options = new Options();

		options.addOption("f", "file", true, "Filename of the tileset to fix. Default: input.png");
		options.addOption("o", "out", true, "Filename of the fixed tileset. Default: fixed.png");
		options.addOption("m", true, "Margin of the existing tileset. Default: 2");
		options.addOption("s", true, "Spacing of the existing tileset. Default: 3");
		options.addOption("w", true, "Width of a tile. Default: 34");
		options.addOption("h", true, "Height of a tile. Default: 34");
		options.addOption("u", "usage", false, "HELP !!!");

		final BasicParser parser = new BasicParser();
		final CommandLine cl = parser.parse(options, args);

		try {
			if (cl.hasOption('u')) {
				final HelpFormatter f = new HelpFormatter();
				f.printHelp("EasyTilesetFixer4AndEngine-Based: TMXTiledMapArtifactFixer by Nicolas Gramlich", options);
			} else {
				final String filename = cl.getOptionValue("f","input.png");
				//FOR DEVELOP //final String filename = cl.getOptionValue("f","dist"+File.separator+"input.png");
				final String outFilename = cl.getOptionValue("o","fixed.png");
				//FOR DEVELOP//final String outFilename = cl.getOptionValue("o","dist"+File.separator+"fixed.png");
				final int tileWidth = Integer.parseInt(cl.getOptionValue("w","34"));
				final int tileHeight = Integer.parseInt(cl.getOptionValue("h","34"));
				final int margin = Integer
						.parseInt(cl.getOptionValue("m", "2"));
				final int spacing = Integer.parseInt(cl
						.getOptionValue("s", "3"));
				
				fix(filename, outFilename, tileWidth, tileHeight, margin,
						spacing);

			}
		} catch (final Throwable t) {
			t.printStackTrace();
			final HelpFormatter f = new HelpFormatter();
			f.printHelp("EasyTilesetFixer4AndEngine-Based: TMXTiledMapArtifactFixer by Nicolas Gramlich", options);
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	private static void fix(final String pFilename,
			final String pOutputFilename, final int pTileWidth,
			final int pTileHeight, final int pMargin, final int pSpacing)
			throws IOException {
		final File sourceFile = new File(pFilename);
		if (!sourceFile.isFile()) {
			throw new IllegalArgumentException("Not a file: " + sourceFile.getAbsolutePath());
		}
		System.out.println("EasyTilesetFixer4AndEngine by @LuizCarvalho");
		System.out.println("Avaliable in www.redrails.com.br");
		System.out.println("Fixing...");

		final BufferedImage img = ImageIO.read(sourceFile);
		final int blank = (new Color(0, 0, 0, 0)).getRGB(); //Erase Pixel
		
		final int imageWidth = img.getWidth();
		final int imageHeight = img.getHeight();

		final int columnCount = imageWidth / pTileWidth;
		final int rowCount = imageHeight / pTileHeight;

		final BufferedImage out = new BufferedImage(1 + imageWidth
				+ columnCount, 1 + imageHeight + rowCount,
				BufferedImage.TYPE_INT_ARGB);
		final Graphics g = out.getGraphics();

		int newRow = 1;
		int newColumn = 1;
		
		for (int column = 0; column < columnCount; column++) {
			for (int row = 0; row < rowCount; row++) {

				/* Upper-Left coordinates in the source image. */
				int sx = column * pTileWidth;
				int sy = row * pTileHeight;

				final int sx2 = sx + pTileWidth;
				final int sy2 = sy + pTileHeight;

				/* Upper-Left coordinates in the destination image. */
				final int dx = sx + newColumn;
				final int dy = sy + newRow;

				final int dx2 = dx + pTileWidth;
				final int dy2 = dy + pTileHeight;

				g.drawImage(img, dx, dy, dx2, dy2, sx, sy, sx2, sy2, null);
				out.setRGB(dx, dy, blank);//draw transparent pixel top-left
				out.setRGB(dx+pTileWidth-1, dy, blank);//draw transparent pixel top-right
				out.setRGB(dx, dy+pTileHeight-1, blank);//draw transparent pixel top-left
				out.setRGB(dx+pTileWidth-1, dy+pTileHeight-1, blank);//draw transparent pixel bottom-right
				newRow++;
				
			}
			newRow=1;
			newColumn++;
		}
		System.out.println("Saving... ");
		ImageIO.write(out, "png", pOutputFilename != null ? new File(
				pOutputFilename) : generateOutputFile(sourceFile));
		
		System.out.println("Tileset Fixed save in: "+pOutputFilename);
		System.out.println("done.");
	}

	static File generateOutputFile(final File pSourceFile) {
		return new File(pSourceFile.getParentFile() + File.separator + "fixed_"
				+ pSourceFile.getName());
	}

	static int determineCount(final int pTotalExtent, final int pTileExtent,
			final int pMargin, final int pSpacing) {
		int count = 0;
		int remainingExtent = pTotalExtent;

		remainingExtent -= pMargin * 2;

		while (remainingExtent > 0) {
			remainingExtent -= pTileExtent;
			remainingExtent -= pSpacing;
			count++;
		}

		return count;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
