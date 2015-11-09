import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class limitchar extends PlainDocument {
	private int limit;
	
	public limitchar(int limit1)
	{
		this.limit=limit1;
	}

	public void insertString(int offset,String str,AttributeSet set) throws BadLocationException
	{
		if(str==null)
		{
			return;
		}
		else if((getLength() + str.length())<=limit)
		{	//str = str.toUpperCase();
			super.insertString(offset,str,set);
		}
	}

}


