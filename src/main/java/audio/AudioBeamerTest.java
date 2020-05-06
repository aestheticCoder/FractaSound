package audio;

public class AudioBeamerTest {

    public static void main(String[] args) {
        String audioFilePath = "C:\\Users\\Alex\\Music\\Tchaikovsky-op19-no3-fueillet-d-album.mp3";
        //String audioFilePath = "island_music_x.wav";
        AudioBeamer player = new AudioBeamer();
        player.streamFile(audioFilePath);
        //player.playMP3Option2(audioFilePath);
    }
}
