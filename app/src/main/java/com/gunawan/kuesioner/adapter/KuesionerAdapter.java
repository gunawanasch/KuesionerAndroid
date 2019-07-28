package com.gunawan.kuesioner.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gunawan.kuesioner.R;
import com.gunawan.kuesioner.model.Kuesioner;

import java.util.List;

public class KuesionerAdapter extends RecyclerView.Adapter<KuesionerAdapter.ViewHolder> {
    private List<Kuesioner> kuesioner;
    private Activity activity;
    private OnCheckedChangeListener listener;

    public KuesionerAdapter(Activity activity, List<Kuesioner> kuesioner) {
        this.kuesioner = kuesioner;
        this.activity = activity;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChange(int position, int idTitle, int idQuestion, String value);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) { this.listener = listener; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.row_kuesioner, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final KuesionerAdapter.ViewHolder viewHolder, final int position) {
        if(position == 0) {
            viewHolder.tvTitle.setVisibility(View.VISIBLE);
            viewHolder.tvTitle.setText(kuesioner.get(position).getTitle());
        }
        else {
            if(kuesioner.get(position).getIdTitle() == kuesioner.get(position-1).getIdTitle())  {
                viewHolder.tvTitle.setVisibility(View.GONE);
            }
            else {
                viewHolder.tvTitle.setVisibility(View.VISIBLE);
                viewHolder.tvTitle.setText(kuesioner.get(position).getTitle());
            }
        }
        viewHolder.tvQuestion.setText(kuesioner.get(position).getQuestion());
        viewHolder.rgAnswer.setTag(position);
        viewHolder.rgAnswer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) group.findViewById(radioButtonId);
                int posAnswer = (int) viewHolder.rgAnswer.getTag();
                kuesioner.get(posAnswer).setSelectedRadioButtonId(radioButtonId);
                if (rb != null) {
                    Log.d("pos answer", "pos list = "+position+", idTitle = "+kuesioner.get(posAnswer).getIdTitle()+", idQuestion = "+kuesioner.get(posAnswer).getIdQuestion()+", value = "+rb.getText().toString());
                    listener.onCheckedChange(posAnswer, kuesioner.get(posAnswer).getIdTitle(), kuesioner.get(posAnswer).getIdQuestion(), rb.getText().toString());
                }
            }
        });
        viewHolder.rgAnswer.check(kuesioner.get(position).getSelectedRadioButtonId());
    }

    @Override
    public int getItemCount() {
        return (null != kuesioner ? kuesioner.size() : 0);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvQuestion;
        private RadioGroup rgAnswer;

        public ViewHolder(View view) {
            super(view);
            tvTitle         = (TextView) view.findViewById(R.id.tvTitle);
            tvQuestion      = (TextView) view.findViewById(R.id.tvQuestion);
            rgAnswer        = (RadioGroup) view.findViewById(R.id.rgAnswer);
            rgAnswer.clearCheck();
            rgAnswer.check(-1);
        }
    }
}