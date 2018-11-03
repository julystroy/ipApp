package com.cartoon.http;

import java.util.List;

public class PaginatedData<T> {
    public boolean end;
    public List<T> items;
    public T ad;
}
