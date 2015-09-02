package com.nathanhaze.locationtagnote.test;

import com.nathanhaze.locationtagnote.AddNote;
import com.nathanhaze.locationtagnote.R;
import com.nathanhaze.locationtagnote.ShowList;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.test.ActivityUnitTestCase;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;

public class AddNoteTest extends ActivityUnitTestCase<AddNote> {

	
    private AddNote mFirstTestActivity;
    private EditText mFirstTestText;
    private Button mClickMeButton;
    private Intent mLaunchIntent;
    public  AddNoteTest () {
    	super(AddNote.class);
        
        
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), AddNote.class);
       // startActivity(mLaunchIntent, null, null);
        final Button launchNextButton =
                (Button) getActivity()
                .findViewById(R.id.save);
        
        mFirstTestText =
                (EditText) mFirstTestActivity
                .findViewById(R.id.note);
        /*
        setActivityInitialTouchMode(true);

        mFirstTestActivity = getActivity();
        
        mClickMeButton = (Button) 
        		mFirstTestActivity
                .findViewById(R.id.save);
        
        mFirstTestText =
                (EditText) mFirstTestActivity
                .findViewById(R.id.note);
         */
    }
    
    @MediumTest
    public void testTextEditText(){
    	getInstrumentation().runOnMainSync(new Runnable() {
    	    @Override
    	    public void run() {
    	    	mFirstTestText.requestFocus();
    	    }
    	});
    	getInstrumentation().waitForIdleSync();
    	getInstrumentation().sendStringSync("Hello Android!");
    	getInstrumentation().waitForIdleSync();
    }
    
    
    @MediumTest
    public void testNextActivityWasLaunchedWithIntent() {
    	
    	/*
        startActivity(mLaunchIntent, null, null);
        final Button launchNextButton =
                (Button) getActivity()
                .findViewById(R.id.save);
        launchNextButton.performClick();

        final Intent launchIntent = getStartedActivityIntent();
        assertNotNull("Intent was null", launchIntent);
        assertTrue(isFinishCalled());

        final String payload =
                launchIntent.getStringExtra(ShowList.EXTRAS_PAYLOAD_KEY);
        assertEquals("Payload is empty", LaunchActivity.STRING_PAYLOAD, payload);
        */
    }
    
    @MediumTest
    public void testClickMeButton_clickButtonAndExpectInfoText() {
      //  String expectedInfoText = mFirstTestActivity.getString(R.string.info_text);
        TouchUtils.clickView(this, mClickMeButton);
      //  assertTrue(View.VISIBLE == mInfoTextView.getVisibility());
      //  assertEquals(expectedInfoText, mInfoTextView.getText());
    }
    
    
    @MediumTest
    public void testClickMeButton_layout() {
        final View decorView =  mFirstTestActivity.getWindow().getDecorView();

        ViewAsserts.assertOnScreen(decorView, mClickMeButton);

        final ViewGroup.LayoutParams layoutParams =
                mClickMeButton.getLayoutParams();
        
        assertNotNull(layoutParams);
        assertEquals(layoutParams.width, WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(layoutParams.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }
    
    
    public void testPreconditions() {
        assertNotNull("mFirstTestActivity is null", mFirstTestActivity);
        assertNotNull("mFirstTestText is null", mFirstTestText);
        testMyFirstTestTextView_labelText();
    }
    
    public void testMyFirstTestTextView_labelText() {
        final String expected = "";
        final String actual = mFirstTestText.getText().toString();
        assertEquals(expected, actual);
    }
}
