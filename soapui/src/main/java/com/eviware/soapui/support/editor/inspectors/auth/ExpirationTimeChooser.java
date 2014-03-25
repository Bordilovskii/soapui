package com.eviware.soapui.support.editor.inspectors.auth;

import com.eviware.soapui.impl.rest.OAuth2Profile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ExpirationTimeChooser extends JPanel
{

	static final String SERVER_EXPIRATION_RADIO_NAME = "serverExpirationRadio";
	static final String MANUAL_EXPIRATION_RADIO_NAME = "manualExpirationRadio";
	static final String TIME_FIELD_NAME = "timeField";
	static final String TIME_UNIT_COMBO_NAME = "timeUnitCombo";

	private JRadioButton serverExpirationTimeOption;
	private JRadioButton manualExpirationTimeOption;
	private JTextField timeTextField;
	private JComboBox timeUnitCombo;

	ExpirationTimeChooser( OAuth2Profile profile )
	{
		setLayout( new BorderLayout( 0, 0 ) );
		initializeRadioButtons();
		JPanel timeSelectionPanel = createTimeSelectionPanel();
		JPanel northPanel = new JPanel( new GridLayout( 3, 1, 0, 0 ) );
		northPanel.add( serverExpirationTimeOption );
		northPanel.add( manualExpirationTimeOption );
		northPanel.add( timeSelectionPanel );
		add( northPanel, BorderLayout.NORTH );

		JPanel centerPanel = new JPanel( new BorderLayout( 0, 0 ) );
		JLabel label = new JLabel( "<html>Here you can set an expiry time if the OAuth2 server doesn't,<br/>so that the token retrieval can be automated.</html>" );
		label.setForeground( new Color(143, 143, 143) );
		centerPanel.add( label, BorderLayout.NORTH );
		add( centerPanel, BorderLayout.CENTER );

	}

	private JPanel createTimeSelectionPanel()
	{
		JPanel timeSelectionPanel = new JPanel( new FlowLayout( FlowLayout.LEFT, 3, 0 ) );
		timeTextField = new JTextField( 5 );
		timeTextField.setName( TIME_FIELD_NAME );
		timeTextField.setHorizontalAlignment( JTextField.RIGHT );
		timeTextField.setText( "30" );
		timeTextField.setEnabled( false );
		timeSelectionPanel.add( timeTextField );
		timeUnitCombo = new JComboBox( new Object[] {
				new TimeUnitOption( 1, "Seconds" ), new TimeUnitOption( 60, "Minutes" ), new TimeUnitOption( 3600, "Hours" ) } );
		timeUnitCombo.setName( TIME_UNIT_COMBO_NAME );
		timeSelectionPanel.add( timeUnitCombo );
		timeUnitCombo.setEnabled( false );
		return timeSelectionPanel;
	}

	private void initializeRadioButtons()
	{
		ButtonGroup buttonGroup = new ButtonGroup();
		serverExpirationTimeOption = new JRadioButton( "Use expiration time from access token", true );
		serverExpirationTimeOption.setName(SERVER_EXPIRATION_RADIO_NAME);
		ActionListener checkBoxMonitor = new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				timeTextField.setEnabled( manualExpirationTimeOption.isSelected() );
				timeUnitCombo.setEnabled( manualExpirationTimeOption.isSelected() );
			}
		};
		serverExpirationTimeOption.addActionListener( checkBoxMonitor );

		manualExpirationTimeOption = new JRadioButton( "Manual" );
		manualExpirationTimeOption.setName(MANUAL_EXPIRATION_RADIO_NAME);
		manualExpirationTimeOption.addActionListener( checkBoxMonitor );

		buttonGroup.add( serverExpirationTimeOption );
		buttonGroup.add( manualExpirationTimeOption );
	}

	public int getAccessTokenExpirationTimeInSeconds()
	{
		TimeUnitOption unit = ( TimeUnitOption )timeUnitCombo.getSelectedItem();
		try
		{
			return serverExpirationTimeOption.isSelected() ? -1 : Integer.parseInt(timeTextField.getText()) * unit.seconds;
		}
		catch( NumberFormatException e )
		{
			return -1;
		}
	}

	private class TimeUnitOption {
		public final int seconds;
		public final String name;

		private TimeUnitOption( int seconds, String name )
		{
			this.seconds = seconds;
			this.name = name;
		}

		@Override
		public String toString()
		{
			return name;
		}
	}
}
