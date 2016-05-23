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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.data.Account;
import com.jinguanguke.guwangjinlai.model.entity.User;
import com.jinguanguke.guwangjinlai.ui.activity.AgreementActivity;
import com.jinguanguke.guwangjinlai.ui.activity.JifenActivity;
import com.jinguanguke.guwangjinlai.util.Utils;
import com.smartydroid.android.starter.kit.account.AccountManager;

import java.util.ArrayList;
import java.util.List;


import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.prototypes.CardWithList;
import it.gmariotti.cardslib.library.prototypes.LinearListView;

/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class GoogleNowWeatherCard extends CardWithList {

    public GoogleNowWeatherCard(Context context) {
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
        header.setTitle("个人信息"); //should use R.string.
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

        User user = AccountManager.getCurrentAccount();
        String my_phone = user.getUserid();
        String my_jointime = user.getJointime();
        //Add an object to the list

        InfosObject w1= new InfosObject(this);
        w1.content = my_phone;
        w1.weatherIcon = R.drawable.ic_settings_phone_light_blue_400_18dp;
        w1.setObjectId(w1.content); //It can be important to set ad id
        mObjects.add(w1);

        InfosObject w2= new InfosObject(this);
        my_jointime = Utils.TimeStamp2Date(my_jointime, "yyyy年MM月dd日");
        w2.content = my_jointime + "  注册";
        w2.weatherIcon = R.drawable.ic_query_builder_light_blue_400_18dp;
        w2.setObjectId(w2.content);
        w2.setSwipeable(false);

        //Example onSwipe
        /*w2.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipe(ListObject object,boolean dismissRight) {
                Toast.makeText(getContext(), "Swipe on " + object.getObjectId(), Toast.LENGTH_SHORT).show();
            }
        });*/
        mObjects.add(w2);

        InfosObject w3= new InfosObject(this);
        w3.content ="使用协议";

        w3.weatherIcon = R.drawable.ic_description_light_blue_400_18dp;
        w3.setObjectId(w3.content);
        mObjects.add(w3);

        InfosObject i4 = new InfosObject(this);
        i4.content = "积分";
        i4.weatherIcon = R.drawable.ic_redeem_light_blue_400_18dp;
        i4.setObjectId(i4.content);
        mObjects.add(i4);

        return mObjects;
    }

    @Override
    public View setupChildView(int childPosition, ListObject object, View convertView, ViewGroup parent) {

        //Setup the ui elements inside the item
        TextView city = (TextView) convertView.findViewById(R.id.carddemo_weather_city);
        ImageView icon = (ImageView) convertView.findViewById(R.id.carddemo_weather_icon);

//
//        //Retrieve the values from the object
        InfosObject weatherObject= (InfosObject)object;
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

    public class InfosObject extends DefaultListObject {

        public String content;
        public int weatherIcon;


        public InfosObject(Card parentCard){
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
                        case "使用协议":
                        {
                            Intent intent = new Intent(view.getContext(),AgreementActivity.class);
                            view.getContext().startActivity(intent);
                        }
                        case "积分":
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
