package audio;

public class AudioBeamerTest {

    public static void main(String[] args) {
        String audioFilePath = "C:\\CS Stuff\\Capstone\\FractaSound\\src\\audio\\island_music_x.wav";
        //String audioFilePath = "island_music_x.wav";
        AudioBeamer player = new AudioBeamer();
        player.streamFile(audioFilePath);
    }
}
