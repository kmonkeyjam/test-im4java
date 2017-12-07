package convert;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import java.io.File;
import java.io.IOException;

public class Main {
  @Argument(alias = "s", description = "Source Directory", required = true)
  private static String srcDir;

  @Argument(alias = "d", description = "Destination Directory", required = true)
  private static String destDir;

  @Argument(alias = "n", description = "Dry Run")
  private static boolean dryRun = false;

  public static void main(String[] args) throws InterruptedException, IOException, IM4JavaException {
    Args.parseOrExit(Main.class, args);

    File folder = new File(srcDir);
    File[] images = folder.listFiles();

    ConvertCmd cmd = new ConvertCmd();

    IMOperation op = new IMOperation();
    op.addImage();
    op.resize(3840);
    op.addImage();

    convertFiles(images, cmd, op, srcDir, destDir);
  }

  private static void convertFiles(File[] images, ConvertCmd cmd, IMOperation op, String srcDir, String destDir) throws IOException, InterruptedException, IM4JavaException {
    srcDir = fixDir(srcDir);
    destDir = fixDir(destDir);

    for (File srcFile : images) {
      if (srcFile.isDirectory()) {
        convertFiles(srcFile.listFiles(), cmd, op, srcDir + srcFile.getName(), destDir + srcFile.getName());
      } else {
        String srcImage = srcFile.getName();
        String dstImage = getDestName(srcImage);
        String destFileName = destDir + dstImage;

        if (new File(destFileName).exists()) {
          System.out.println("Already converted file: " + destFileName);
        } else if (srcImage.endsWith("tif") || srcImage.endsWith("jpg")) {
          System.out.println(String.format("Converting from %s to %s", srcDir + srcImage, destFileName));
          File destDirFile = new File(destDir);
          if (!destDirFile.exists()) {
            destDirFile.mkdirs();
          }
          if (!dryRun) {
            cmd.run(op, srcDir + srcImage, destFileName);
          }
        }
      }
    }
  }

  public static String getDestName(String srcImage) {
    int lastDot = srcImage.lastIndexOf('.');
    return srcImage.substring(0, lastDot) + ".jpg";
  }

  private static String fixDir(String dir) {
    if (dir.charAt(dir.length() - 1) != '/') {
      return dir + "/";
    }
    return dir;
  }
}
