import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.ByteOrder;

public class VSJQueryMixers {
    public VSJQueryMixers() {}
    public static void main(String[]
                                    args)throws Exception {
        if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN)) {
            System.out.println("System is Big-endian");
        } else {
            System.out.println("System is Little-endian");
        }
        showMixers();
    }
    public static void showMixers() {
        ArrayList<Mixer.Info>
                mixInfos =
                new ArrayList<>(
                        Arrays.asList(
                                AudioSystem.getMixerInfo(
                                )));
        Line.Info sourceDLInfo =
                new Line.Info(
                        SourceDataLine.class);
        Line.Info targetDLInfo =
                new Line.Info(
                        TargetDataLine.class);
        Line.Info clipInfo =
                new Line.Info(Clip.class);
        Line.Info portInfo =
                new Line.Info(Port.class);
        String support;
        for (Mixer.Info mixInfo:
                mixInfos) {
            Mixer mixer =
                    AudioSystem.getMixer(
                            mixInfo);
            support = ", supports ";
            if (mixer.isLineSupported(
                    sourceDLInfo))
                support +=
                        "SourceDataLine ";
            if (mixer.isLineSupported(
                    clipInfo))
                support += "Clip ";
            if (mixer.isLineSupported(
                    targetDLInfo))
                support +=
                        "TargetDataLine ";
            if (mixer.isLineSupported(
                    portInfo))
                support += "Port ";
            System.out.println("Mixer: "
                    + mixInfo.getName() +
                    support + ", " +
                    mixInfo.getDescription(
                    ));
        } } }
        // https://www.developerfusion.com/article/84314/wired-for-sound/