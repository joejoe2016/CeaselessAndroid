package org.theotech.ceaselessandroid.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.theotech.ceaselessandroid.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.BlurTransformation;

/**
 *  created by travis Feb. 2016
 */

public class HTFDemoScriptureFragment extends Fragment implements HTFDemoFragment {
    private static final String TAG = HTFDemoScriptureFragment.class.getSimpleName();

    @Bind(R.id.verse_image)
    ImageView verseImage;
    @Bind(R.id.verse_image_reflection)
    ImageView verseImageReflection;
    @Bind(R.id.verse_title_and_share)
    LinearLayout verseTitleAndShare;
    @Bind(R.id.verse_title)
    TextView verseTitle;
    @Bind(R.id.verse_text)
    TextView verseText;
    @Bind(R.id.right_arrow)
    TextView rightArrow;
    @Bind(R.id.tool_tip_layout)
    LinearLayout toolTipLayout;
    @Bind(R.id.learn_from_scripture)
    RelativeLayout learnFromScripture;
    @Bind(R.id.swipe_to_continue)
    RelativeLayout swipeToContinue;

    public HTFDemoScriptureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create view and bind
        View view = inflater.inflate(R.layout.fragment_htfdemo_scripture, container, false);
        ButterKnife.bind(this, view);

        drawVerseImage();

        // verse title and text
        populateVerse(getString(R.string.default_verse_citation), getString(R.string.default_verse_text));
/*
        toolTipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolTipLayout.setVisibility(View.INVISIBLE);
            }
        });
*/
        animate();

        return view;
    }

    private void drawVerseImage() {
        List<Transformation> transformations = new ArrayList<>();

        transformations.add(new BlurTransformation(getActivity(), 25, 4));

        Picasso.with(getActivity()).load(R.drawable.at_the_beach)
                .fit()
                .centerCrop()
                .into(verseImage);

        Picasso.with(getActivity()).load(R.drawable.at_the_beach)
                .fit()
                .centerCrop()
                .transform(transformations)
                .into(verseImageReflection);
    }

    private void populateVerse(String citation, String text) {
        verseTitle.setText(citation);
        verseText.setText(text);
    }

    private void animate() {
        animateTooltip();
        animateSwipeToContinue();
        animateRightArrow();
    }

    private void animateRightArrow() {
        LinearInterpolator interpolator = new LinearInterpolator();
        TranslateAnimation mTrAnimation = new TranslateAnimation(
                -4, 4, 0, 0);
        mTrAnimation.setDuration(250);
        mTrAnimation.setStartOffset(20);
        mTrAnimation.setRepeatCount(-1);
        mTrAnimation.setRepeatMode(Animation.REVERSE);
        mTrAnimation.setInterpolator(interpolator);
        rightArrow.setAnimation(mTrAnimation);
    }

    private void animateTooltip() {
        LinearInterpolator interpolator = new LinearInterpolator();
        AlphaAnimation mAlAnimation = new AlphaAnimation(0, 1);
        mAlAnimation.setDuration(600);
        mAlAnimation.setStartOffset(500);
        mAlAnimation.setInterpolator(interpolator);
        toolTipLayout.setAnimation(mAlAnimation);
    }

    private void animateSwipeToContinue() {
        LinearInterpolator interpolator = new LinearInterpolator();
        AlphaAnimation mAlAnimation = new AlphaAnimation(0, 1);
        mAlAnimation.setDuration(600);
        mAlAnimation.setStartOffset(1800);
        mAlAnimation.setInterpolator(interpolator);
        swipeToContinue.setAnimation(mAlAnimation);
    }

    public void onSelected() {

//       toolTipLayout.setVisibility(View.VISIBLE);
    }

}
