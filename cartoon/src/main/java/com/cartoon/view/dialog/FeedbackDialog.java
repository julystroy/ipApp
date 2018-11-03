package com.cartoon.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.cartoon.data.FeedbackReason;

import java.util.ArrayList;
import java.util.List;

import cn.com.xuanjiezhimen.R;


/**
 * Created by wusue on 17/4/11.
 */

public class FeedbackDialog extends Dialog {
    public String suggestion = "";
    public String QQNum = "";
    public Button submitBtn;
    public List<String> mReasons = new ArrayList<String>();

    public FeedbackDialog(Context context, int theme) {
        super(context, theme);
    }

    public FeedbackDialog(Context context) {
        super(context);
    }

    public static class Builder {
        private Context context;
        private OnClickListener onClickCancelListener;
        private OnClickListener onClickSubmitListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setOnClickCancelListener(OnClickListener listener) {
            this.onClickCancelListener = listener;
            return this;
        }

        public Builder setOnClickSubmitListener(OnClickListener listener) {
            this.onClickSubmitListener = listener;
            return this;
        }

        public void checkSubmitEnable(FeedbackDialog dialog) {
            if (dialog.mReasons == null || dialog.mReasons.size() == 0) {
                if (dialog.submitBtn.isEnabled())
                    dialog.submitBtn.setEnabled(false);
            } else if (!dialog.submitBtn.isEnabled()) {
                dialog.submitBtn.setEnabled(true);
            }
        }

        private void initViews(View layout, View.OnClickListener listener, List<FeedbackReason> feedbackReasons) {
            //创建view；
            TableRow tableRow = null;
            for (int i = 0; i < feedbackReasons.size(); i++) {

                CheckBox checkbox = (CheckBox) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.feedback_checkbox, null).findViewById(R.id.example);

                checkbox.setText(feedbackReasons.get(i).reason);
                checkbox.setTag(feedbackReasons.get(i).id);
                checkbox.setOnClickListener(listener);
                if (i % 2 == 0) {
                    tableRow = new TableRow(context);
                    TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 0, 0, 25);

                    tableRow.addView(checkbox);
                    TableLayout tableLayout = (TableLayout) layout.findViewById(R.id.tb_reasons);
                    tableLayout.addView(tableRow, lp);
                } else {

                    if (tableRow != null) {
                        tableRow.addView(checkbox);
                    }
                }
            }
        }

        public FeedbackDialog create(List<FeedbackReason> feedbackReasons) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final FeedbackDialog dialog = new FeedbackDialog(context, R.style.Dialog);

            View layout = inflater.inflate(R.layout.dialog_feedback, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            dialog.submitBtn = (Button) layout.findViewById(R.id.btn_submit);

            View.OnClickListener checkboxClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String selectedReason = String.valueOf(v.getTag());
                    if (((CheckBox) v).isChecked()) {
                        // 添加
                        if (!dialog.mReasons.contains(selectedReason)) {
                            dialog.mReasons.add(selectedReason);
                        }
                    } else {
                        // 删除
                        if (dialog.mReasons.contains(selectedReason)) {
                            dialog.mReasons.remove(selectedReason);
                        }
                    }
                    checkSubmitEnable(dialog);
                }
            };

            initViews(layout, checkboxClickListener, feedbackReasons);

            final EditText sug = (EditText) layout.findViewById(R.id.et_feedback);
            sug.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    dialog.suggestion = sug.getText().toString();
                }
            });

            final EditText QQNum = (EditText) layout.findViewById(R.id.et_QQNum);
            QQNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    dialog.QQNum = QQNum.getText().toString();
                }
            });

            if (onClickCancelListener != null) {
                layout.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                        onClickCancelListener.onClick(dialog, 0);
                    }
                });
            }
            if (onClickSubmitListener != null) {
                layout.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                        onClickSubmitListener.onClick(dialog, 1);
                    }
                });
            }
            return dialog;
        }
    }
}
