package com.ensten.robert.realtimedatabase;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Robert on 10/04/2017.
 */

public class Graphs extends Fragment {


    int[] colors = {R.color.GreenYellow, R.color.OrangeRed, R.color.DeepPink, R.color.Purple, R.color.DeepSkyBlue, R.color.Chocolate};
    private TextView tTarjetas, tBuscadores, tRedesSociales, tBoca, tLocalizacion, tOtros, totalCl;

    String[] data = {"0", "0", "0", "0", "0", "0"};

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mBuscadoresRef = mRootRef.child("buscadores");
    DatabaseReference mTarjetasRef = mRootRef.child("tarjetas");
    DatabaseReference mRedesRef = mRootRef.child("redes");
    DatabaseReference mBocaRef = mRootRef.child("boca");
    DatabaseReference mLocalizacionRef = mRootRef.child("localizacion");
    DatabaseReference mOtrosRef = mRootRef.child("otros");
    PieChart pieChart;
    Button buscButton, tarjButton, socialButton, bocaButton, localButton, otrosButton;
    Boolean bBusc, bTarj, bRedes, bBoca, bLocal, bOtros;

    public Graphs() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment2_graph, container, false);

       /* tBuscadores = (TextView) rootView.findViewById(R.id.textViewBuscadores);
        tTarjetas = (TextView) rootView.findViewById(R.id.textViewTarjetas);
        tRedesSociales = (TextView) rootView.findViewById(R.id.textViewRedesSociales);
        tBoca = (TextView) rootView.findViewById(R.id.textViewBoca);
        tLocalizacion = (TextView) rootView.findViewById(R.id.textViewLocalizacion);
        tOtros = (TextView) rootView.findViewById(R.id.textViewOtros);*/

        totalCl = (TextView) rootView.findViewById(R.id.totalClientes);
        pieChart = (PieChart) rootView.findViewById(R.id.piechartId);

        buscButton = (Button) rootView.findViewById(R.id.buscadoresButton);
        tarjButton = (Button) rootView.findViewById(R.id.tarjetasButton);
        socialButton = (Button) rootView.findViewById(R.id.socialesButton);
        bocaButton = (Button) rootView.findViewById(R.id.bocaButton);
        localButton = (Button) rootView.findViewById(R.id.localizacionButton);
        otrosButton = (Button) rootView.findViewById(R.id.otrosButton);

        bBusc = true;
        bTarj = true;
        bBoca = true;
        bRedes = true;
        bLocal = true;
        bOtros = true;

        buscButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //change color
                if (bBusc) {
                    buscButton.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.Gray), PorterDuff.Mode.MULTIPLY);
                    bBusc = false;

                } else {
                    buscButton.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.GreenYellow), PorterDuff.Mode.MULTIPLY);
                    bBusc = true;
                }
                readData();
                addEntries();
                //refresh chart
                pieChart.invalidate();
            }
        });

        tarjButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bTarj) {
                    tarjButton.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.Gray), PorterDuff.Mode.MULTIPLY);
                    bTarj = false;
                } else {
                    tarjButton.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.OrangeRed), PorterDuff.Mode.MULTIPLY);
                    bTarj = true;
                }
                readData();
                addEntries();
                pieChart.invalidate();
            }
        });

        socialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bRedes) {
                    socialButton.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.Gray), PorterDuff.Mode.MULTIPLY);
                    bRedes = false;
                } else {
                    socialButton.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.DeepPink), PorterDuff.Mode.MULTIPLY);
                    bRedes = true;
                }
                readData();
                addEntries();
                pieChart.invalidate();
            }
        });

        bocaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bBoca) {
                    bocaButton.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.Gray), PorterDuff.Mode.MULTIPLY);
                    bBoca = false;
                } else {
                    bocaButton.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.Purple), PorterDuff.Mode.MULTIPLY);
                    bBoca = true;
                }
                readData();
                addEntries();
                pieChart.invalidate();
            }
        });

        localButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bLocal) {
                    localButton.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.Gray), PorterDuff.Mode.MULTIPLY);
                    bLocal = false;
                } else {
                    localButton.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.DeepSkyBlue), PorterDuff.Mode.MULTIPLY);
                    bLocal = true;
                }
                readData();
                addEntries();
                pieChart.invalidate();
            }
        });

        otrosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bOtros) {
                    otrosButton.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.Gray), PorterDuff.Mode.MULTIPLY);
                    bOtros = false;
                } else {
                    otrosButton.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.Chocolate), PorterDuff.Mode.MULTIPLY);
                    bOtros = true;
                }
                readData();
                addEntries();
                pieChart.invalidate();
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        readData();
    }

    protected void readData(){

        //Set array values to 0
        for (int i = 0; i < data.length; i++) {
            data[i] = "0";
        }

        //read all data from database
        mBuscadoresRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                //tBuscadores.setText(text);
                if (bBusc)
                    data[0] = text;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mTarjetasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                //tTarjetas.setText(text);
                if (bTarj)
                    data[1] = text;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRedesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                //tRedesSociales.setText(text);
                if (bRedes)
                    data[2] = text;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mBocaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                //tBoca.setText(text);
                if (bBoca)
                    data[3] = text;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mLocalizacionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                //tLocalizacion.setText(text);
                if(bLocal)
                    data[4] = text;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mOtrosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                //tOtros.setText(text);

                if(bOtros)
                    data[5] = text;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //get total
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                addEntries();
                int total = 0;
                for (int i = 0; i < data.length; i++) {
                    total = total + Integer.parseInt(data[i]);
                }
                totalCl.setText("Un total de " + total + " clientes han sido preguntados");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected void addEntries() {
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < data.length; i++) {

            // turn your data into Entry objects
            entries.add(new PieEntry(Integer.parseInt(data[i]), i));
        }
        PieDataSet dataset = new PieDataSet(entries, "");

        dataset.setValueFormatter(new PercentFormatter());
        /*ArrayList<String> labels = new ArrayList<String>();
        labels.add("Buscadores");
        labels.add("Tarjetas");
        labels.add("Redes Sociales");
        labels.add("Boca a Boca");
        labels.add("Localización");
        labels.add("Otros");*/


        PieData data = new PieData(dataset);
        dataset.setColors(colors, getContext());
        pieChart.setRotationEnabled(false);
        pieChart.setData(data);
        pieChart.notifyDataSetChanged(); // let the chart know it's data changed
        pieChart.invalidate();//refresh
        pieChart.notifyDataSetChanged();
        pieChart.setDrawHoleEnabled(false);

        pieChart.setNoDataText("");
        pieChart.setDrawSliceText(false);

        pieChart.setUsePercentValues(true);

        Legend legend = pieChart.getLegend();

        legend.setEnabled(false);
    }
    //Format class to change values to %
    public class PercentFormatter implements IValueFormatter, IAxisValueFormatter
    {

        protected DecimalFormat mFormat;

        public PercentFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0");
        }

        /**
         * Allow a custom decimalformat
         *
         * @param format
         */
        public PercentFormatter(DecimalFormat format) {
            this.mFormat = format;
        }

        // IValueFormatter
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value) + " %";
        }

        // IAxisValueFormatter
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(value) + " %";
        }

        public int getDecimalDigits() {
            return 1;
        }
    }
}
