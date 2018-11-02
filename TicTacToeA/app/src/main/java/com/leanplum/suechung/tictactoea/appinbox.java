package com.leanplum.suechung.tictactoea;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.leanplum.Leanplum;
import com.leanplum.LeanplumInbox;
import com.leanplum.callbacks.InboxChangedCallback;
import com.leanplum.LeanplumActivityHelper;
import com.leanplum.Var;
import com.leanplum.annotations.Parser;
import com.leanplum.annotations.Variable;
import com.leanplum.callbacks.VariablesChangedCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.leanplum.Leanplum;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.lang.Object;
import java.util.AbstractMap;
import java.util.concurrent.ConcurrentHashMap;
import java.net.URISyntaxException;
import java.net.URI;
import android.text.TextUtils;


public class appinbox extends Activity {

    private static final String TEXT1 = "text1";
    private static final String TEXT2 = "text2";
    public LeanplumInbox inbox = Leanplum.getInbox();
    public List<String> messagesIds = inbox.messagesIds();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inboxlist);

        Leanplum.advanceTo("appinbox");


        final ListView listView = (ListView) findViewById(R.id.listView);
        ListAdapter listAdapter = createListAdapter();
        listView.setAdapter(listAdapter);
        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Leanplum.track("App Inbox Item Clicked!");

                // checking if the message is unread
                if (!inbox.messageForId(messagesIds.get(i)).isRead()) {
                    Log.i("Leanplum", "Reading getMessageId: " + messagesIds.get(i));
                    inbox.messageForId(messagesIds.get(i)).read();

                } else {
                    // Looks like we cannot mark it as unread once is read
                    Log.i("Leanplum", messagesIds.get(i) + " is already read!");
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Leanplum.track("App Inbox Long Press!");
                Log.i("Leanplum", "Deleting getMessageId: " + messagesIds.get(i));
                inbox.messageForId(messagesIds.get(i)).remove();
                return false;
            }
        });

        inbox.addChangedHandler(new InboxChangedCallback() {
            @Override
            public void inboxChanged() {
                ListAdapter updatedListAdapter = createListAdapter();
                listView.setAdapter(updatedListAdapter);
            }
        });



    }

    private ListAdapter createListAdapter() {
        String[] fromMapKey = new String[] {TEXT1, TEXT2};
        int[] toLayoutId = new int[] {android.R.id.text1, android.R.id.text2};
        List<Map<String, String>> list = inboxToListItems();
        return new SimpleAdapter(this, list, android.R.layout.simple_list_item_2, fromMapKey, toLayoutId);
    }

    private List<Map<String, String>> inboxToListItems() {
        List<Map<String, String>> listItem = new ArrayList<>(inbox.count());
        messagesIds = inbox.messagesIds();
        for (String messageId : messagesIds) {
            Map<String, String> listItemMap = new HashMap<>();

            if (!inbox.messageForId(messageId).isRead()) {
                listItemMap.put(TEXT1, "N - " + inbox.messageForId(messageId).getTitle());
            } else {
                listItemMap.put(TEXT1, inbox.messageForId(messageId).getTitle());
            }
            listItemMap.put(TEXT2, inbox.messageForId(messageId).getSubtitle());
            listItem.add(Collections.unmodifiableMap(listItemMap));
        }
        return Collections.unmodifiableList(listItem);
    }


    public void backbutton(View view) {
        Intent backbutton = new Intent(this, MainActivity.class);
        startActivity(backbutton);
    }





    private static void setPageDimensions(HashMap<String, Object> attrMap,
                                          String[] value, int startIndex) {
        StringBuilder pageName = new StringBuilder();
        for (int i = startIndex; i < value.length; i++) {
            pageName.append(value[i]);
            pageName.append("/");
        }
        attrMap.put("pageName", pageName.toString());
    }
    public static void sendLeanplumScreenView(String screenName, ConcurrentHashMap<String, String > dimsValue) throws URISyntaxException {
        HashMap<String, Object> attributeMap = new HashMap<>();
        String segments[];
        if(screenName.startsWith("/")) {
            final String prefixHost = "https://www.ticketmonster.com";
            screenName = new URI(prefixHost+screenName).getPath();

            if(!TextUtils.isEmpty(screenName)) {
                segments = screenName.split("/");
                if(segments.length > 1) {
                    screenName = segments[1];
                    if(!"deals".equalsIgnoreCase(screenName)) {
                        if(segments.length > 3) {
                            setPageDimensions(attributeMap,segments,2);
                        }
                    }
                }
            }
        } else if (screenName.split("\\.").length > 1){
            segments = screenName.split("\\.");
            screenName = segments[0];
            setPageDimensions(attributeMap,segments,1);
        }

        if(dimsValue!= null && !dimsValue.isEmpty()) {
            attributeMap.putAll(dimsValue);
        }

        if(attributeMap.isEmpty()) {
            Leanplum.advanceTo(screenName);
            android.util.Log.d("TEST","send screenNames."+screenName);
        } else {
            Leanplum.advanceTo(screenName,attributeMap);
            android.util.Log.d("TEST","send screenNames."+screenName);
        }

    }


}