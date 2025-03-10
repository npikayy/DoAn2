package khang.doan2_tnn.services;
import com.mpatric.mp3agic.Mp3File;
public class AudioUtils {
    public static long getAudioDurationInSeconds(String filePath) {
        try {
            Mp3File mp3File = new Mp3File(filePath);
            return mp3File.getLengthInSeconds();
        } catch (Exception e) {
            return 0;
        }
    }
}
