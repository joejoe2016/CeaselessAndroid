package org.theotech.ceaselessandroid.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.analytics.Tracker;
import com.tokenautocomplete.TokenCompleteTextView;

import org.theotech.ceaselessandroid.CeaselessApplication;
import org.theotech.ceaselessandroid.R;
import org.theotech.ceaselessandroid.cache.CacheManager;
import org.theotech.ceaselessandroid.cache.LocalDailyCacheManagerImpl;
import org.theotech.ceaselessandroid.note.NoteManager;
import org.theotech.ceaselessandroid.note.NoteManagerImpl;
import org.theotech.ceaselessandroid.person.PersonManager;
import org.theotech.ceaselessandroid.person.PersonManagerImpl;
import org.theotech.ceaselessandroid.realm.pojo.NotePOJO;
import org.theotech.ceaselessandroid.realm.pojo.PersonPOJO;
import org.theotech.ceaselessandroid.util.AnalyticsUtils;
import org.theotech.ceaselessandroid.util.CommonUtils;
import org.theotech.ceaselessandroid.util.Constants;
import org.theotech.ceaselessandroid.view.PersonsCompletionView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNoteFragment extends Fragment {

    @BindView(R.id.note_tags)
    PersonsCompletionView noteTags;
    @BindView(R.id.add_note_text)
    EditText noteText;
    @BindView(R.id.cancel_note)
    Button cancelNote;
    @BindView(R.id.save_note)
    Button saveNote;

    private List<PersonPOJO> taggedPeople = new ArrayList<>();
    private PersonManager personManager = null;
    private NoteManager noteManager = null;
    private CacheManager cacheManager = null;
    private String noteId = null;
    private Tracker mTracker;

    private static final String TAG = AddNoteFragment.class.getSimpleName();

    public AddNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);

        taggedPeople = new ArrayList<>();
        personManager = PersonManagerImpl.getInstance(getActivity());
        noteManager = NoteManagerImpl.getInstance(getActivity());
        cacheManager = LocalDailyCacheManagerImpl.getInstance(getActivity());

        CeaselessApplication application = (CeaselessApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // set title
        getActivity().setTitle(getString(R.string.person_add_note));

        // create view and bind
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);
        ButterKnife.bind(this, view);

        // add current person to the list of taggedPeople (if we're on a page that shows a person)
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(Constants.PERSON_ID_BUNDLE_ARG)) {
                String personId = bundle.getString(Constants.PERSON_ID_BUNDLE_ARG);
                if (personId != null) {
                    noteTags.addObject(personManager.getPerson(personId));
                }
            }
            if (bundle.containsKey(Constants.NOTE_ID_BUNDLE_ARG)) {
                noteId = bundle.getString(Constants.NOTE_ID_BUNDLE_ARG);
                NotePOJO note = noteManager.getNote(noteId);
                noteText.setText(note.getText());
                if (note.getPeopleTagged() != null) {
                    for (String personId : note.getPeopleTagged()) {
                        noteTags.addObject(personManager.getPerson(personId));
                    }
                }
            }
        }

        // wire the note tags
        List<PersonPOJO> allPersonPOJOs = personManager.getActivePeople();
        noteTags.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, allPersonPOJOs));
        noteTags.setTokenListener(new TokenCompleteTextView.TokenListener<PersonPOJO>() {
            @Override
            public void onTokenAdded(PersonPOJO token) {
                taggedPeople.add(token);
            }

            @Override
            public void onTokenRemoved(PersonPOJO token) {
                taggedPeople.remove(token);
            }
        });

        // wire the buttons
        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if (noteId != null) {
                    noteManager.editNote(noteId, null, noteText.getText().toString(), taggedPeople);
                } else {
                    noteManager.addNote(null, noteText.getText().toString(), taggedPeople);
                }
                // simulate back button press to exit this fragment
                getActivity().onBackPressed();
            }
        });
        cancelNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();

                // simulate back button press to exit this fragment
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    private void hideKeyboard() {
        CommonUtils.hideKeyboard(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        AnalyticsUtils.sendScreenViewHit(mTracker, "NoteScreen");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void onPause() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        super.onPause();
    }
}
