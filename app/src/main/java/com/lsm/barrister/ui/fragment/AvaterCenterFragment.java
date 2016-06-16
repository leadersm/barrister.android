package com.lsm.barrister.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsm.barrister.R;
import com.lsm.barrister.data.entity.User;
import com.lsm.barrister.ui.activity.AppointmentSettingsActivity;
import com.lsm.barrister.ui.activity.AvatarDetailActivity;
import com.lsm.barrister.ui.activity.LoginActivity;
import com.lsm.barrister.ui.activity.MsgsActivity;
import com.lsm.barrister.ui.activity.MyAccountActivity;
import com.lsm.barrister.ui.activity.SettingsActivity;

public class AvaterCenterFragment extends Fragment {

    public AvaterCenterFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(User user) {
        AvaterCenterFragment fragment = new AvaterCenterFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }


    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("user");
        }
    }

    AQuery aq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_avater_center, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        aq = new AQuery(view);

        aq.id(R.id.btn_user_layout).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), AvatarDetailActivity.class);
                    startActivity(intent);
                }
            }
        });

        SimpleDraweeView usrIconView = (SimpleDraweeView) view.findViewById(R.id.image_avatar);

        if(!TextUtils.isEmpty(user.getUserIcon()))
            usrIconView.setImageURI(Uri.parse(user.getUserIcon()));

        aq.id(R.id.tv_nickname).text(user.getName());

        aq.id(R.id.btn_my_account).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), MyAccountActivity.class);
                    startActivity(intent);
                }
            }
        });

        aq.id(R.id.btn_my_bispeak_settings).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AppointmentSettingsActivity.class);
                startActivity(intent);
            }
        });

        aq.id(R.id.btn_my_msg).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MsgsActivity.class);
                startActivity(intent);
            }
        });

        aq.id(R.id.btn_contact_us).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4009889595"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        aq.id(R.id.btn_settings).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

    }

}
