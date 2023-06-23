package ru.dto.issued;

import ru.dto.bookIss.BookIssued;
import ru.dto.bookIss.BookStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IssuedStorage {
    private final List<Issued> storage = new ArrayList<>();

    public List<Issued> getIssStorage() {
        return storage;
    }

    public void addIss(Issued issued) {
        storage.add(issued);
    }

    public void addListIssue(List<Issued> listIssued) {
        this.storage.addAll(listIssued);
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof IssuedStorage)
        {
            IssuedStorage obj = (IssuedStorage)other;
            return Objects.equals(obj.getIssStorage(), this.getIssStorage());
        }
        else
            return false;
    }
}
