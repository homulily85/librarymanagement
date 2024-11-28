package com.cozyspace.librarymanagement.user;

import com.cozyspace.librarymanagement.datasource.MemberRecord;
import javafx.collections.ObservableList;

public interface SearchMember {

    ObservableList<MemberRecord> searchMember(String keyword);

    ObservableList<MemberRecord> viewAllMember();
}
