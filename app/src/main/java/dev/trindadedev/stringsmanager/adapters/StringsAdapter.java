package dev.trindadedev.stringsmanager.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import dev.trindadedev.stringsmanager.R;
import dev.trindadedev.stringsmanager.classes.SimpleHighlighter;
import dev.trindadedev.stringsmanager.classes.SyntaxScheme;
import dev.trindadedev.stringsmanager.StringsManagerApp;
import dev.trindadedev.stringsmanager.activities.MainActivity;
import dev.trindadedev.stringsmanager.classes.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;

@Deprecated
public class StringsAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<HashMap<String, Object>> mData;
    private LayoutInflater mInflater;
    ListView listS;
    MaterialAlertDialogBuilder actions_dialog;
    AlertDialog actions_dialogAlert;

    public StringsAdapter(Context contexto, ArrayList<HashMap<String, Object>> dados, ListView listSs) {
        mContext = contexto;
        mData = dados;
        mInflater = LayoutInflater.from(contexto);
        listS = listSs;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public HashMap<String, Object> getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView textViewValue;
        MaterialCardView card;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.content_string, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewValue = convertView.findViewById(R.id.val);
            viewHolder.card = convertView.findViewById(R.id.card);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        viewHolder.textViewValue.setText(String.valueOf(mData.get(position).get("val")));
        new SimpleHighlighter(viewHolder.textViewValue, "xml");
        
        viewHolder.card.setOnLongClickListener(v ->{
            if(StringsManagerApp.getOrientation(mContext).equals("portrait")) {
                bottomsheet_actions(position);
            }else{
                dialog_actions(position);    
            }
            return true;
        });
        
        viewHolder.card.setOnClickListener(v ->{
             TextUtils.copy(mContext, mData.get(position).get("val").toString());
             Toast.makeText(mContext, mContext.getResources().getString(R.string.copied), 4000).show();
        });

        return convertView;
    }
    private void dialog_actions(int position) {
    	         actions_dialog = new MaterialAlertDialogBuilder(mContext);
                 View weiv = ((Activity)mContext).getLayoutInflater().inflate(R.layout.content_bottom_sheet_actions, null);
                 actions_dialog.setView(weiv);
                 actions_dialog.setCancelable(true);
                 actions_dialogAlert = actions_dialog.create();
                 actions_dialogAlert.show();
                 Button edit = weiv.findViewById(R.id.edit);
                 Button delete = weiv.findViewById(R.id.delete);
                 edit.setOnClickListener(vv ->{
                     actions_dialogAlert.dismiss();
                     MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(mContext);
                     View alertD = ((Activity)mContext).getLayoutInflater().inflate(R.layout.content_dialog_create_edit, null);
                     dialog.setView(alertD);

                     final TextInputEditText stringName =  alertD.findViewById(R.id.stringName);
                     final TextInputEditText stringValue = alertD.findViewById(R.id.stringValue);
                     final TextInputLayout til1 = alertD.findViewById(R.id.til1);
                     final TextInputLayout til2 = alertD.findViewById(R.id.til2);
                     stringName.setText(mData.get(position).get("name").toString());
                     stringValue.setText(mData.get(position).get("value").toString());
                     stringName.setFocusableInTouchMode(true);
                     stringValue.setFocusableInTouchMode(true);
                     til1.setBoxCornerRadii((float) 12, (float) 12, (float) 12, (float) 12);
                     til2.setBoxCornerRadii((float) 12, (float) 12, (float) 12, (float) 12);
                     dialog.setTitle(((Activity)mContext).getResources().getString(R.string.edit));
                     dialog.setPositiveButton(((Activity)mContext).getResources().getString(R.string.save), (d, w) ->{
                        String v = "<string name=\"" + stringName.getText().toString() + "\">" + stringValue.getText().toString()  + "</string>";
                        mData.get((int)position).put("val", v);
                        mData.get((int)position).put("name", stringName.getText().toString());
                        mData.get((int)position).put("value", stringValue.getText().toString());
                        StringsManagerApp.updateListView(mContext, mData, listS);
                      });
                     dialog.setNegativeButton(((Activity)mContext).getResources().getString(R.string.cancel), (d, w) ->{           
                     });
                    dialog.show();       
                 });
                 delete.setOnClickListener(vv ->{
                     actions_dialogAlert.dismiss();
                     MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(mContext);
                     dialog.setTitle(mContext.getResources().getString(R.string.delete));
                     dialog.setMessage(mContext.getResources().getString(R.string.delete_dialog_message));
                     dialog.setPositiveButton(mContext.getResources().getString(R.string.delete), (d, w) ->{
                     mData.remove(position);
                     StringsManagerApp.updateListView(mContext, mData, listS);
                 });
                dialog.setNegativeButton(mContext.getResources().getString(R.string.cancel), (d, w) ->{
                });
            dialog.show();
            });
    }
    
    private void bottomsheet_actions(int position) {
    	final BottomSheetDialog actions_bottom = new BottomSheetDialog(mContext);
                final BottomSheetBehavior actions_bottomB;
                View weiv;
                weiv = ((Activity)mContext).getLayoutInflater().inflate(R.layout.content_bottom_sheet_actions, null);
                actions_bottom.setContentView(weiv);
                actions_bottom.setCancelable(true);
                Button edit = weiv.findViewById(R.id.edit);
                Button delete = weiv.findViewById(R.id.delete);
                edit.setOnClickListener(vv ->{
                actions_bottom.dismiss();
                     MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(mContext);
                     View alertD = ((Activity)mContext).getLayoutInflater().inflate(R.layout.content_dialog_create_edit, null);
                     dialog.setView(alertD);
                     

                     final TextInputEditText stringName =  alertD.findViewById(R.id.stringName);
                     final TextInputEditText stringValue = alertD.findViewById(R.id.stringValue);
                     final TextInputLayout til1 = alertD.findViewById(R.id.til1);
                     final TextInputLayout til2 = alertD.findViewById(R.id.til2);
                     stringName.setText(mData.get(position).get("name").toString());
                     stringValue.setText(mData.get(position).get("value").toString());
                     stringName.setFocusableInTouchMode(true);
                     stringValue.setFocusableInTouchMode(true);
                     til1.setBoxCornerRadii((float) 12, (float) 12, (float) 12, (float) 12);
                     til2.setBoxCornerRadii((float) 12, (float) 12, (float) 12, (float) 12);
                     dialog.setTitle(((Activity)mContext).getResources().getString(R.string.edit));
                     dialog.setPositiveButton(((Activity)mContext).getResources().getString(R.string.save), (d, w) ->{
                        String v = "<string name=\"" + stringName.getText().toString() + "\">" + stringValue.getText().toString()  + "</string>";
                        mData.get((int)position).put("val", v);
                        mData.get((int)position).put("name", stringName.getText().toString());
                        mData.get((int)position).put("value", stringValue.getText().toString());
                        StringsManagerApp.updateListView(mContext, mData, listS);
                      });
                     dialog.setNegativeButton(((Activity)mContext).getResources().getString(R.string.cancel), (d, w) ->{           
                     });
                    dialog.show();       
                });
                delete.setOnClickListener(vv ->{
                     actions_bottom.dismiss();
                     MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(mContext);
                     dialog.setTitle(mContext.getResources().getString(R.string.delete));
                     dialog.setMessage(mContext.getResources().getString(R.string.delete_dialog_message));
                     dialog.setPositiveButton(mContext.getResources().getString(R.string.delete), (d, w) ->{
                     mData.remove(position);
                     StringsManagerApp.updateListView(mContext, mData, listS);
                 });
                dialog.setNegativeButton(mContext.getResources().getString(R.string.cancel), (d, w) ->{
                });
            dialog.show();
            });
            actions_bottom.show();
    }
}