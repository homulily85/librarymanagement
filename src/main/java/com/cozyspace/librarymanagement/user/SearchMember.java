package com.cozyspace.librarymanagement.user;

import com.cozyspace.librarymanagement.datasource.MemberRecord;
import javafx.collections.ObservableList;

public interface SearchMember {
    ObservableList<MemberRecord> searchMemberByName(String name);

    ObservableList<MemberRecord> searchMemberByEmail(String email);

    ObservableList<MemberRecord> searchMemberByPhone(String phone);

    ObservableList<MemberRecord> viewAllMember();
}
