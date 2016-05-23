/*
 * ******************************************************************************
 *   Copyright (c) 2013-2014 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */

package com.jinguanguke.guwangjinlai.ui.cards;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.model.entity.User;
import com.jinguanguke.guwangjinlai.ui.activity.AgreementActivity;
import com.jinguanguke.guwangjinlai.ui.activity.JifenActivity;
import com.jinguanguke.guwangjinlai.util.Utils;
import com.smartydroid.android.starter.kit.account.AccountManager;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.prototypes.CardWithList;
import it.gmariotti.cardslib.library.prototypes.LinearListView;

/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class SettingsCard extends CardWithList {

    public SettingsCard(Context context) {
        super(context);
    }

    @Override
    protected CardHeader initCardHeader() {

        //Add Header
        CardHeader header = new CardHeader(getContext(), R.layout.carddemo_googlenowweather_inner_header);

        //Add a popup menu. This method set OverFlow button to visible
//        header.setPopupMenu(R.menu.popup_item, new CardHeader.OnClickCardHeaderPopupMenuListener() {
//            @Override
//            public void onMenuItemClick(BaseCard card, MenuItem item) {
//
//                switch (item.getItemId()){
//                    case R.id.action_add:
//                        //Example: add an item
//                        WeatherObject w1= new WeatherObject(GoogleNowWeatherCard.this);
//                        w1.city ="Madrid";
//                        w1.temperature = 24;
//                        w1.weatherIcon = R.drawable.ic_action_sun;
//                        w1.setObjectId(w1.city);
//                        mLinearListAdapter.add(w1);
//                        break;
//                    case R.id.action_remove:
//                        //Example: remove an item
//                        mLinearListAdapter.remove(mLinearListAdapter.getItem(0));
//                        break;
//                }
//
//            }
//        });
        header.setTitle("设置"); //should use R.string.
        return header;
    }

    @Override
    protected void initCard() {

        //Set the whole card as swipeable
        setSwipeable(true);
        setOnSwipeListener(new OnSwipeListener() {
            @Override
            public void onSwipe(Card card) {
                Toast.makeText(getContext(), "Swipe on " + card.getCardHeader().getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected List<ListObject> initChildren() {

        //Init the list
        List<ListObject> mObjects = new ArrayList<ListObject>();


        SettingsObject w1= new SettingsObject(this);
        w1.content = "清除缓存";
        w1.weatherIcon = R.drawable.ic_delete_forever_light_blue_400_18dp;
        w1.setObjectId(w1.content);
        mObjects.add(w1);

        SettingsObject w2= new SettingsObject(this);
        w2.content = "退出";
        w2.weatherIcon = R.drawable.ic_highlight_off_light_blue_400_18dp;
        w2.setObjectId(w2.content);
        w2.setSwipeable(false);


        mObjects.add(w2);



        return mObjects;
    }

    @Override
    public View setupChildView(int childPosition, ListObject object, View convertView, ViewGroup parent) {

        //Setup the ui elements inside the item
        TextView city = (TextView) convertView.findViewById(R.id.carddemo_weather_city);
        ImageView icon = (ImageView) convertView.findViewById(R.id.carddemo_weather_icon);

//
//        //Retrieve the values from the object
        SettingsObject weatherObject= (SettingsObject)object;
        icon.setImageResource(weatherObject.weatherIcon);
        city.setText(weatherObject.content);
        return  convertView;
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.carddemo_googlenowweather_inner_main;
    }



    // -------------------------------------------------------------
    // Weather Object
    // -------------------------------------------------------------

    public class SettingsObject extends DefaultListObject {

        public String content;
        public int weatherIcon;


        public SettingsObject(Card parentCard){
            super(parentCard);
            init();
        }

        private void init(){
            //OnClick Listener
            setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(LinearListView parent, View view, int position, ListObject object) {
                    switch (getObjectId())
                    {
                        case "清除缓存":
                        {
                            //AccountManager.registerObserver(new Logout());
                            AccountManager.logout();
                        }
                        case "退出":
                        {
                            Intent intent = new Intent(view.getContext(),JifenActivity.class);
                            view.getContext().startActivity(intent);
                        }
                    }
                }
            });
//
//            //OnItemSwipeListener
//            setOnItemSwipeListener(new OnItemSwipeListener() {
//                @Override
//                public void onItemSwipe(ListObject object, boolean dismissRight) {
//                    Toast.makeText(getContext(), "Swipe on " + object.getObjectId(), Toast.LENGTH_SHORT).show();
//                }
//            });
        }

    }


}
