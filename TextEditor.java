//THE IMPORTED LIBRARIES
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import java.io.*;
import java.util.*;

class TextEditor extends JFrame
{
	//DECLARATION OF ALL THE OBJECTS USED IN THIS APPLICATION
	JMenuBar mb=new JMenuBar();
	JMenu []m=new JMenu[4];
	JMenuItem []item=new JMenuItem[13];
	final JPopupMenu pm=new JPopupMenu();
	JMenuItem []mi=new JMenuItem[5];
	JCheckBoxMenuItem cb= new JCheckBoxMenuItem("Word wrap");
	JTextArea t=new JTextArea();
	Font f=new Font("arial",2,16);
	JFileChooser fc=new JFileChooser();
	Image icon=Toolkit.getDefaultToolkit().getImage("image/Editor.png");
	String findString,wholeText,filename=null;
	int ind=0;
	boolean open=false;
	
	//DEFAULT CONSTRUCTOR OF THE TEXTEDITOR APPLICATION
	public TextEditor()
	{
		super("Untitled-TextEditor"); //SETTING DEFAULT TITLE OF THE FRAME
		setSize(700,500);			//SETTING DEFAULT SIZE OF THE FRAME
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//SETTING CLOSE OPERATION TO THE APPLICATION
		setIconImage(icon);
		t.setFont(f);
		add(t);
		JScrollPane sp=new JScrollPane(t,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(sp);
		addMenu();	//ADDING addMenu() METHOD TO THE CONSTRUCTOR
		addPopup();	//ADDING addPopup() METHOD TO THE CONSTRUCTOR
		addListener();	//ADDING addListener() METHOD TO THE CONSTRUCTOR
		cb.setState(true);	//SETTING THE DEFAULT STATE OF WORDWRAP METHOD
		t.setLineWrap(true);	//SETTING WORDWRAP AS TRUE OR FALSE
		setVisible(true);	//MAKING THE FRAME VISIBLE
	}
	
	//addMenu METHOD OF THE CONSTRUCTOR
	void addMenu()
	{
		setJMenuBar(mb);	//ADDING MAIN MENUBAR TO THE FRAME
		String []str={"File","Edit","Format","Help"};
		for(int i=0;i<m.length;i++)
		{
			m[i]=new JMenu(str[i]);
			mb.add(m[i]);
		}
		char p[]={'F','E','O','H'};
		for(int i=0;i<m.length;i++)
		{
			m[i].setMnemonic(p[i]);
		}
		addItems();
	}
	
	//addItems METHOD FOR ADDING MENUITEMS TO THE MAIN MENU
	void addItems()
	{
		String []str={"New","Open...","Save","Save As...","Exit","Cut","Copy","Paste","Delete","Select All","Date/Time","Font...","About TextEditor"};
		int x=0;
		for(int i=0;i<item.length;i++)
		{
			item[i]=new JMenuItem(str[i]);
			if(i==5||i==11||i==12)
				x++;
			m[x].add(item[i]);
			m[2].add(cb);
			if(i==3||i==7||i==9)
				m[x].addSeparator();
		}
		char p[]={'N','O','S','A','x','U','t','C','p','l','A','D','F','A'};
		for(int i=0;i<item.length;i++)
		{				
			item[i].setMnemonic(p[i]);
			cb.setMnemonic('W');
		}
		
		//SETTING ACCELERATOR KEYS OF SOME MENUITEMS IN THE MENU
		item[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
		item[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
		item[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		item[5].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));
		item[6].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
		item[7].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK));
		item[9].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,ActionEvent.CTRL_MASK));
	}
	
	//addPopup METHOD FOR ADDING POPUP MENU
	void addPopup()
	{
		String str[]={"Cut","Copy","Paste","Delete","Select All"};
		for(int i=0;i<mi.length;i++)
		{
			mi[i]=new JMenuItem(str[i]);
			pm.add(mi[i]);
			if(i==2||i==3)
				pm.addSeparator();
		}
		
		//ADDING MOUSELISTENER TO THE RIGHT CLICK FOR THE POPUPLISTENER
		t.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent evt)
			{
				showPopup(evt);
			}
			public void mouseReleased(MouseEvent evt)
			{
				showPopup(evt);
			}
			private void showPopup(MouseEvent evt)
			{
				if (evt.isPopupTrigger())
				{
					pm.show(evt.getComponent(),evt.getX(),evt.getY());
				}
			}
		});
	}
	
	//addListener METHOD FOR ADDING LISTENERS TO THE MENUITEMS IN THE MAIN MENU
	void addListener()
	{
		item[0].addActionListener(new TextListener());
		item[1].addActionListener(new TextListener());
		item[2].addActionListener(new TextListener());
		item[3].addActionListener(new TextListener());
		item[4].addActionListener(new TextListener());
		item[5].addActionListener(new TextListener());
		item[6].addActionListener(new TextListener());
		item[7].addActionListener(new TextListener());
		item[8].addActionListener(new TextListener());
		item[9].addActionListener(new TextListener());
		item[10].addActionListener(new TextListener());
		item[11].addActionListener(new TextListener());
		cb.addActionListener(new TextListener());
		item[12].addActionListener(new TextListener());
		mi[0].addActionListener(new TextListener());
		mi[1].addActionListener(new TextListener());
		mi[2].addActionListener(new TextListener());
		mi[3].addActionListener(new TextListener());
		mi[4].addActionListener(new TextListener());
		
		//ADDING WINDOWLISTENER TO HANDLE CLOSE WINDOW EVENT
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent evt)
			{
				exitEditor();
			}
		});
	}
	
	//CLASS FOR HANDLING ALL EVENTS OF THE TEXT EDITOR
	class TextListener implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			if(evt.getSource()==item[0])	//ACTION FOR NEW MENU OPTION OF FILE MENU
				newFile();
			if(evt.getSource()==item[1])	//ACTION FOR OPEN MENU OPTION OF FILE MENU
				open();
			if(evt.getSource()==item[2])	//ACTION FOR SAVE MENU OPTION OF FILE MENU
				save();
			if(evt.getSource()==item[3])	//ACTION FOR SAVEAS MENU OPTION OF FILE MENU
			{
				open=false;
				save();
			}
			if(evt.getSource()==item[4])	//ACTION FOR EXIT MENU OPTION OF FILE MENU
				exitEditor();
			if(evt.getSource()==item[5]||evt.getSource()==mi[0])	//ACTION FOR CUT MENU OPTION OF EDIT MENU AND POPUPMENU	
				t.cut();
			if(evt.getSource()==item[6]||evt.getSource()==mi[1])	//ACTION FOR COPY MENU OPTION OF EDIT MENU AND POPUPMENU
				t.copy();
			if(evt.getSource()==item[7]||evt.getSource()==mi[2])	//ACTION FOR PASTE MENU OPTION OF EDIT MENU AND POPUPMENU
				t.paste();
			if(evt.getSource()==item[8]||evt.getSource()==mi[3])	//ACTION FOR DELETE MENU OPTION OF EDIT MENU AND POPUPMENU
				t.replaceSelection(null);
			if(evt.getSource()==item[9]||evt.getSource()==mi[4])	//ACTION FOR SELECTALL MENU OPTION OF EDIT MENU AND POPUPMENU
				t.selectAll();
			if(evt.getSource()==item[10])	//ACTION FOR TIME/DATE MENU OPTION OF EDIT MENU 
				date();
			if(evt.getSource()==item[11])	//ACTION FOR FONT MENU OPTION OF FORMAT MENU 
				font();
			if(evt.getSource()==cb)		//ACTION FOR WORDWRAP MENU OPTION OF FORMAT MENU 
				wrap();
			if(evt.getSource()==item[12])	//ACTION FOR ABOUTUS MENU OPTION OF ABOUT MENU 
				JOptionPane.showMessageDialog(null,"This is a Text Editor application Program built using Java Language.\n Presented By PIYUSH KHARE","About Text Editor",JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	//METHOD FOR NEW MENU OPTION OF FILE MENU
	void newFile()
	{
		if(!(t.getText().equals(null)))
		{
			open=false;
			int select=JOptionPane.showConfirmDialog(null,"Do you want to Save changes to Untitled?","TextEditor",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
		
			if(select==JOptionPane.YES_OPTION)
				save();
			else if(select==JOptionPane.NO_OPTION)
			{
				t.setText(null);
				setTitle("Untitled-TextEditor");
			}
		}
	}
	
	//METHOD FOR OPEN MENU OPTION OF FILE MENU
	void open()
	{
		int result=fc.showOpenDialog(new Panel());
		if(result==JFileChooser.APPROVE_OPTION)
		{
			filename = String.valueOf(fc.getSelectedFile());
			setTitle(filename);
			open=true;
			try
			{
				FileReader fr=new FileReader(filename);
				BufferedReader br=new BufferedReader(fr);
				String s;
				while((s=br.readLine())!=null)
				{
					t.append(s);
					t.append("\n");
				}
				fr.close();
			}catch(Exception ex){}
		}
	}
	
	//METHOD FOR SAVE MENU OPTION OF FILE MENU
	void save()
	{
		if(open==true)
		{
			try
			{
				FileWriter fw = new FileWriter(filename);
				fw.write(t.getText());
				fw.close();
				open= true;
			}
			catch(Exception ex){}
		}
		else
		{
			int result=fc.showSaveDialog(new Panel());
			File f = fc.getSelectedFile();
			if(f.exists())
			{
				int select = JOptionPane.showConfirmDialog(this, "This file already exists, Overwrite it?","confirm save as",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
				if(select==JOptionPane.YES_OPTION)
					t.setText(t.getText());
				else if(select==JOptionPane.NO_OPTION)
					save();
				else
					return;
			}
			if(result==JFileChooser.APPROVE_OPTION)
			{
				
				filename=String.valueOf(fc.getSelectedFile());
				setTitle(filename);
				try
				{
					FileWriter fw=new FileWriter(filename);
					fw.write(t.getText());
					fw.close();
					open=true;
				}catch(Exception ex){}
			}
		}
	}
	
	//METHOD FOR EXIT MENU OPTION OF FILE MENU AND CLOSE WINDOW BUTTON
	void exitEditor()
	{
		if(!t.getText().equals(""))
		{
			int confirm = JOptionPane.showConfirmDialog(null,"Text in the file has changed.\n Do you want to save the changes?","Exit",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
			if(confirm == JOptionPane.YES_OPTION )
			{
				save();
				dispose();
				System.exit(0);
			}
			else if(confirm == JOptionPane.NO_OPTION )
			{
				dispose();
				System.exit(0);
			}
		}
		else
		{
			System.exit(0);
		}
    }
	
	//METHOD FOR TIME/DATE MENU OPTION OF EDIT MENU
	void date()
	{
		Date dt=new Date();
		String dd=dt.toString();
		t.setText(dd);
	}
	
	//METHOD FOR FONT MENU OPTION OF FORMAT MENU
	void font()
	{
		new FontChooser().show();	//METHOD FOR DISPLAYING FONTDIALOG BOX
	}
	
	//METHOD FOR WORDWRAP MENU OPTION OF FORMAT MENU
	void wrap()
	{
		if(cb.isSelected())
			t.setLineWrap(true);
		else
			t.setLineWrap(false);
	}
	
	//CLASS FOR BUILDING FONT DIALOG BOX
	class FontChooser extends javax.swing.JDialog
	{
		public static final int RET_CANCEL = 0;
		public static final int RET_OK = 1;
		private Font font;
        public FontChooser(java.awt.Frame parent, Font font)
		{
			super(parent);
			this.font = font;
			initComponents();
			lblPreview.setFont(font);
		}
		public FontChooser(java.awt.Frame parent)
		{
			super(parent);
			this.font = new Font("Dialog",Font.PLAIN,12);
			initComponents();
			lblPreview.setFont(font);
		}
		public FontChooser(Font font)
		{
			super((javax.swing.JFrame)null);
			this.font = font;
			initComponents();
			lblPreview.setFont(font);
		}
		public FontChooser()
		{
			super((javax.swing.JFrame)null);
			this.font = new Font("Dialog",Font.PLAIN,12);
			initComponents();
			lblPreview.setFont(font);
		}
		public Font getFont()
		{
			return font;
		}
		public int getReturnStatus()
		{
			return returnStatus;
		}
		private void initComponents()
		{
			//DECLARATION OF ALL VARIABLES USED IN FONTCHOOSER CLASS
			java.awt.GridBagConstraints gridBagConstraints;
			mainPanel = new javax.swing.JPanel();
			fontPanel = new javax.swing.JPanel();
			jLabel1 = new javax.swing.JLabel();
			jLabel2 = new javax.swing.JLabel();
			jLabel3 = new javax.swing.JLabel();
			jScrollPane1 = new javax.swing.JScrollPane();
			lstFont = new javax.swing.JList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
			jScrollPane2 = new javax.swing.JScrollPane();
			lstStyle = new javax.swing.JList();
			jScrollPane3 = new javax.swing.JScrollPane();
			lstSize = new javax.swing.JList();
			previewPanel = new javax.swing.JPanel();
			lblPreview = new javax.swing.JLabel();
			buttonPanel = new javax.swing.JPanel();
			okButton = new javax.swing.JButton();
			cancelButton = new javax.swing.JButton();
			setTitle("Select Font");
			setModal(true);
			setResizable(false);
			addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent evt)
				{
					closeDialog(evt);
				}
			});
			mainPanel.setLayout(new java.awt.GridLayout(2, 1));
			fontPanel.setLayout(new java.awt.GridBagLayout());
			jLabel1.setText("Font");
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
			gridBagConstraints.weightx = 2.0;
			fontPanel.add(jLabel1, gridBagConstraints);
			jLabel2.setText("Style");
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
			fontPanel.add(jLabel2, gridBagConstraints);
			jLabel3.setText("Size");
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
			gridBagConstraints.weightx = 0.2;
			fontPanel.add(jLabel3, gridBagConstraints);
			lstFont.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			lstFont.addListSelectionListener(new javax.swing.event.ListSelectionListener()
			{
				public void valueChanged(javax.swing.event.ListSelectionEvent evt)
				{
					lstFontValueChanged(evt);
				}
			});
			jScrollPane1.setViewportView(lstFont);
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.ipadx = 1;
			gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
			gridBagConstraints.weightx = 2.0;
			fontPanel.add(jScrollPane1, gridBagConstraints);
			lstStyle.setModel(new javax.swing.AbstractListModel()
			{
				String[] strings = { "Plain", "Bold", "Italic", "Bold Italic" };
				public int getSize()
				{
					return strings.length;
				}
				public Object getElementAt(int i)
				{
					return strings[i]; 
				}
			});
			lstStyle.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			lstStyle.addListSelectionListener(new javax.swing.event.ListSelectionListener()
			{
				public void valueChanged(javax.swing.event.ListSelectionEvent evt)
				{
					lstStyleValueChanged(evt);
				}
			});
			jScrollPane2.setViewportView(lstStyle);
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.ipadx = 1;
			gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
			fontPanel.add(jScrollPane2, gridBagConstraints);
			lstSize.setModel(new javax.swing.AbstractListModel()
			{
				String[] strings = { "8", "10", "11", "12", "14", "16", "20", "24", "28", "36", "48", "72", "96" };
				public int getSize()
				{
					return strings.length;
				}
				public Object getElementAt(int i)
				{
					return strings[i];
				}
			});
			lstSize.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			lstSize.addListSelectionListener(new javax.swing.event.ListSelectionListener()
			{
				public void valueChanged(javax.swing.event.ListSelectionEvent evt)
				{
					lstSizeValueChanged(evt);
				}
			});
			jScrollPane3.setViewportView(lstSize);
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 2;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.ipadx = 1;
			gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
			gridBagConstraints.weightx = 0.2;
			fontPanel.add(jScrollPane3, gridBagConstraints);
			mainPanel.add(fontPanel);
			previewPanel.setLayout(new java.awt.BorderLayout());
			previewPanel.setBorder(new javax.swing.border.TitledBorder(null, "Preview", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12)));
			lblPreview.setFont(new java.awt.Font("Dialog", 0, 12));
			lblPreview.setText("AaBbCc");
			previewPanel.add(lblPreview, java.awt.BorderLayout.CENTER);
			mainPanel.add(previewPanel);
			getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);
			buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
			okButton.setText("OK");
			okButton.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent evt)
				{
					okButtonActionPerformed(evt);
				}
			});
			buttonPanel.add(okButton);
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent evt)
				{
					cancelButtonActionPerformed(evt);
				}
			});
			buttonPanel.add(cancelButton);
			getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
			pack();
			java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			setSize(new java.awt.Dimension(443, 429));
			setLocation((screenSize.width-443)/2,(screenSize.height-429)/2);
		}
		private void lstStyleValueChanged(javax.swing.event.ListSelectionEvent evt)
		{
			int style = -1;
			String selStyle = (String)lstStyle.getSelectedValue();
			if(selStyle=="Plain")
				style = Font.PLAIN;
			if(selStyle=="Bold")
				style = Font.BOLD;
			if(selStyle=="Italic")
				style = Font.ITALIC;
			if(selStyle=="Bold Italic")
				style = Font.BOLD + Font.ITALIC;
			font = new Font(font.getFamily(),style,font.getSize());
			lblPreview.setFont(font);
		}
		private void lstSizeValueChanged(javax.swing.event.ListSelectionEvent evt)
		{
			int size = Integer.parseInt((String)lstSize.getSelectedValue());
			font = new Font(font.getFamily(),font.getStyle(),size);
			lblPreview.setFont(font);
		}
		private void lstFontValueChanged(javax.swing.event.ListSelectionEvent evt)
		{
			font = new Font((String)lstFont.getSelectedValue(),font.getStyle(),font.getSize());
			lblPreview.setFont(font);
		}    
		private void okButtonActionPerformed(java.awt.event.ActionEvent evt)
		{
			doClose(RET_OK);
		}    
		private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)
		{
			doClose(RET_CANCEL);
		}    
		private void closeDialog(java.awt.event.WindowEvent evt)
		{
			doClose(RET_CANCEL);
		}
        public void doClose(int retStatus)
		{
			returnStatus = retStatus;
			t.setFont(getFont());
			setVisible(false);
		}
        private javax.swing.JPanel fontPanel;
		private javax.swing.JScrollPane jScrollPane1;
		private javax.swing.JLabel jLabel1;
		private javax.swing.JLabel jLabel3;
		private javax.swing.JLabel jLabel2;
		private javax.swing.JList lstSize;
		private javax.swing.JButton okButton;
		private javax.swing.JList lstFont;
		private javax.swing.JScrollPane jScrollPane2;
		private javax.swing.JList lstStyle;
		private javax.swing.JPanel mainPanel;
		private javax.swing.JButton cancelButton;
		private javax.swing.JPanel previewPanel;
		private javax.swing.JLabel lblPreview;
		private javax.swing.JPanel buttonPanel;
		private javax.swing.JScrollPane jScrollPane3;
        private int returnStatus = RET_CANCEL;
	}// END OF FONTCHOOSER CLASS OF FONT DIALOG BOX
	
	//MAIN METHOD OF TEXTEDITOR CLASS
	public static void main(String args[])
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception ex){}
		new TextEditor();
	}
}//END OF TEXTEDITOR CLASS