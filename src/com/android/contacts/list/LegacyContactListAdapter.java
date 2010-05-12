/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.contacts.list;

import android.app.patterns.CursorLoader;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts.People;
import android.provider.ContactsContract.Contacts;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A cursor adapter for the People.CONTENT_TYPE content type.
 */
@SuppressWarnings("deprecation")
public class LegacyContactListAdapter extends ContactEntryListAdapter {

    static final String[] PEOPLE_PROJECTION = new String[] {
        People._ID,                         // 0
        People.DISPLAY_NAME,                // 1
        People.PHONETIC_NAME,               // 2
        People.STARRED,                     // 3
        People.PRESENCE_STATUS,             // 4
    };

    protected static final int PERSON_ID_COLUMN_INDEX = 0;
    protected static final int PERSON_DISPLAY_NAME_COLUMN_INDEX = 1;
    protected static final int PERSON_PHONETIC_NAME_COLUMN_INDEX = 2;
    protected static final int PERSON_STARRED_COLUMN_INDEX = 3;
    protected static final int PERSON_PRESENCE_STATUS_COLUMN_INDEX = 4;

    private CharSequence mUnknownNameText;

    public LegacyContactListAdapter(Context context) {
        super(context);
        mUnknownNameText = context.getText(android.R.string.unknownName);
    }

    @Override
    public void configureLoader(CursorLoader loader) {
        loader.setUri(People.CONTENT_URI);
        loader.setProjection(PEOPLE_PROJECTION);
        loader.setSortOrder(People.DISPLAY_NAME);
    }

    public boolean isContactStarred() {
        return getCursor().getInt(PERSON_STARRED_COLUMN_INDEX) != 0;
    }

    @Override
    public String getContactDisplayName() {
        return getCursor().getString(PERSON_DISPLAY_NAME_COLUMN_INDEX);
    }

    public Uri getPersonUri() {
        Cursor cursor = getCursor();
        long personId = cursor.getLong(PERSON_ID_COLUMN_INDEX);
        return ContentUris.withAppendedId(People.CONTENT_URI, personId);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final ContactListItemView view = new ContactListItemView(context, null);
        view.setUnknownNameText(mUnknownNameText);
        return view;
    }

    @Override
    public void bindView(View itemView, Context context, Cursor cursor) {
        ContactListItemView view = (ContactListItemView)itemView;
        bindName(view, cursor);
        bindPresence(view, cursor);
    }

    protected void bindName(final ContactListItemView view, Cursor cursor) {
        view.showDisplayName(cursor, PERSON_DISPLAY_NAME_COLUMN_INDEX, false, 0);
        view.showPhoneticName(cursor, PERSON_PHONETIC_NAME_COLUMN_INDEX);
    }

    protected void bindPresence(final ContactListItemView view, Cursor cursor) {
        view.showPresence(cursor, PERSON_PRESENCE_STATUS_COLUMN_INDEX);
    }
}