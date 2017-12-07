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

  public static void main(String[] args) throws InterruptedException, IOException, IM4JavaException {
    Args.parseOrExit(Main.class, args);

    srcDir = fixDir(srcDir);
    destDir = fixDir(destDir);
    
    File folder = new File(srcDir);
    File[] images = folder.listFiles();

    File destDirFile = new File(destDir);
    if (!destDirFile.exists()) {
      destDirFile.mkdir();
    }

    ConvertCmd cmd = new ConvertCmd();

    // List<String> images = Collections.singletonList("8. Barnes16.tif");

    IMOperation op = new IMOperation();
    op.addImage();
    op.resize(3840);
    op.addImage();

    for (File srcFile : images) {
      String srcImage = srcFile.getName();
      int lastDot = srcImage.lastIndexOf('.');
      String dstImage =
              srcImage.substring(0, lastDot - 1) + ".jpg";
      System.out.println(String.format("Converting from %s to %s", srcDir + srcImage, destDir + dstImage));
      cmd.run(op, srcDir + srcImage, destDir + dstImage);
    }
  }

  private static String fixDir(String dir) {
    if (dir.charAt(dir.length() - 1) != '/') {
      return dir + "/";
    }
    return dir;
  }
}
