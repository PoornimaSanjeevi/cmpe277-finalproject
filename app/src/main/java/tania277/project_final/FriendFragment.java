package tania277.project_final;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Tania on 11/16/15.
 */
public class FriendFragment extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friend_fragment, container, false);

        Button addfriend = (Button) rootView.findViewById(R.id.addfriend);
        Log.i("message:",""+(addfriend==null));
        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddFriend.class);
//                intent.putExtra("VIDEO_ID", searchResults.get(pos).getId());
                startActivity(intent);
            }
        });
        return rootView;
    }
}
