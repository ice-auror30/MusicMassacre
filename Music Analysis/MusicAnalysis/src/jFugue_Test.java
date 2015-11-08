import org.jfugue.midi.MidiFileManager;
import org.jfugue.midi.MidiParser;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.theory.Note;
import java.io.*;
import javax.sound.midi.*;
public class jFugue_Test {
	public static void main(String args[])throws IOException, InvalidMidiDataException {
		File midi_source = new File("/Users/IceAuror/G39_MusicMassacre/Music Analysis/ryan_ceiling.mid");
		Pattern pattern = MidiFileManager.loadPatternFromMidi(midi_source);
		Player player = new Player();
		//player.play(pattern);
		System.out.println(pattern);
		MidiParser parser = new MidiParser();
		MyParserListener listener = new MyParserListener();
		parser.addParserListener(listener);
		parser.parse(MidiSystem.getSequence(midi_source));
		System.out.println("There are "+ listener.counter_a + " 'A' notes in the file");
		System.out.println("There are "+ listener.counter_b + " 'B' notes in the file");
		System.out.println("There are "+ listener.counter_c + " 'C' notes in the file");
		System.out.println("There are "+ listener.counter_d + " 'D' notes in the file");
		System.out.println("There are "+ listener.counter_e + " 'E' notes in the file");
		System.out.println("There are "+ listener.counter_f + " 'F' notes in the file");
		System.out.println("There are "+ listener.counter_g + " 'G' notes in the file");
	
	}
}
class MyParserListener extends ParserListenerAdapter {
public int counter_c;
public int counter_d;
public int counter_e;
public int counter_f;
public int counter_g;
public int counter_a;
public int counter_b;


@Override
public void onNoteParsed(Note note) {
	// TODO Auto-generated method stub; 1 d, 2 E, 3 f,4 g, 5 a, 6 b  
	switch(note.getPositionInOctave()){
	case(0): counter_c++;
		break;
	case(1): counter_d++;
		break;
	case(2): counter_e++;
	break;
	case(3): counter_f++;
	break;
	case(4): counter_g++;
	break;
	case(5): counter_a++;
	break;
	case(6): counter_b++;
	break;
	}
	}
}

