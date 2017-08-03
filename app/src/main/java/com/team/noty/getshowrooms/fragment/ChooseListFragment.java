package com.team.noty.getshowrooms.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.team.noty.getshowrooms.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChooseListFragment extends Fragment {


    public ChooseListFragment() {
        // Required empty public constructor
    }
    final String ATTRIBUTE_NAME_TEXT = "text";
    String[] city = { "Не важно", "Москва", "Санкт-Петербург", "Екатеринбург",
            "Киев", "Тбилиси", "Баку", "Казань", "Красноярск", "Калининград"};
    String[] region = {"Не важно", "САО","СВАО","СЗАО","ЮАО","ЮВАО","ЮЗАО","ЗелАО","ЦАО","ВАО","ЗАО"};

    String[] metro_station = {"Не важно", "м. Китай-Город","м. Фрунзенская", "м. Черкизовская", "м. Киевская", "м. Электрозаводская",
            "м. Спортивная", "м. Проспект Мира", "м. Красные Ворота", "м. Славянский Бульвар", "м. Таганская", "м. Серпуховская",
            "м. Киевская", "м. Баррикадная", "м. Савеловская", "м. Баррикадная", "м. Баррикадная", "м. Тульская", "м. Лубянка",
            "м. Студенческая", "м. Марксистская", "м. Сухаревская", "м. Охотный Ряд", "м. Лермонтовский проспект", "м. Мякинино",
            "м. Славянский Бульвар", "м. Белорусская", "м. Арбатская", "м. Третьяковская", "м. Маяковская", "м. Курская", "м. Трубная",
            "м. ул. 1905 года", "м. Боровицкая", "м. Чеховская", "м. Новокузнецкая", "м. Динамо", "м. Пушкинская", "м. Полянка",
            "м. Кузнецкий мост", "м. Тверская", "м. Кропоткинская", "м. Цветной Бульвар", "м. Выставочная", "м. Дмитровская",
            "м. Парк Культуры", "м. Краснопресненская", "м. Чистые Пруды", "м. Площадь Революции", "м. Смоленская", "м. Павелецкая"};

    String[] work_time = {"Не важно", "до 18:00", "до 19:00", "до 20:00", "до 21:00", "до 22:00", "до 23:00", "круглосуточно"};

    String[] price = {"\u20BD", "\u20BD \u20BD", "\u20BD \u20BD \u20BD", "\u20BD \u20BD \u20BD \u20BD"};

    ListView listView;
    String nameItem;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_list, container, false);

        sharedPreferences = getActivity().getSharedPreferences("Filter", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        listView = (ListView) view.findViewById(R.id.list_choose);
        Bundle bundle = getArguments();
        if (bundle != null) {
           nameItem = bundle.getString("NameItem");
            assert nameItem != null;
            switch (nameItem)
            {
                case "city":
                    setContent(city);
                    break;
                case "region":
                    setContent(region);
                    break;
                case "station":
                    setContent(metro_station);
                    break;
                case "price":
                    setContent(price);
                    break;
                case "workTime":
                    setContent(work_time);
                    break;
            }
        }


        return view;
    }

    public void setContent(final String[] strings)
    {
        // упаковываем данные в понятную для адаптера структуру
        final ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(
                strings.length);
        Map<String, Object> m;
        for (int i = 0; i < strings.length; i++) {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_NAME_TEXT, strings[i]);
            data.add(m);
        }
        String[] from = { ATTRIBUTE_NAME_TEXT };
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = { R.id.tvText};

        // создаем адаптер
        SimpleAdapter sAdapter = new SimpleAdapter(getActivity(), data, R.layout.item_for_list_choose,
                from, to);
        listView.setAdapter(sAdapter);
        // определяем список и присваиваем ему адаптер
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editor.putString(nameItem, strings[position]).apply();
                getFragmentManager().popBackStack();
            }
        });

    }


}
