package mod.hasrat.control.logic;

import static android.text.TextUtils.isEmpty;
import static com.besome.sketch.SketchApplication.getContext;
import static mod.SketchwareUtil.getDip;

import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.editor.LogicEditorActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.sketchware.remod.R;

import java.util.ArrayList;

import a.a.a.ZB;
import a.a.a.aB;
import a.a.a.bB;
import a.a.a.eC;
import a.a.a.jC;
import a.a.a.uq;
import a.a.a.wB;
import mod.SketchwareUtil;
import mod.hasrat.dialog.SketchDialog;
import mod.hey.studios.util.Helper;

public class LogicClickListener implements View.OnClickListener {

    private final eC projectDataManager;
    private final LogicEditorActivity logicEditor;
    private final ProjectFileBean projectFile;

    public LogicClickListener(LogicEditorActivity logicEditor) {
        this.logicEditor = logicEditor;
        projectDataManager = jC.a(logicEditor.B);
        this.projectFile = logicEditor.M;
    }

    private ArrayList<String> getUsedVariable(int type) {
        return projectDataManager.e(projectFile.getJavaName(), type);
    }

    private ArrayList<String> getUsedList(int type) {
        return projectDataManager.d(projectFile.getJavaName(), type);
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (!isEmpty(tag)) {
            switch (tag) {
                case "listAddCustom":
                    addCustomList();
                    break;

                case "variableAddNew":
                    addCustomVariable();
                    break;

                case "variableRemove":
                    removeVariable();
                    break;

                case "listRemove":
                    removeList();
                    break;
            }
        }
    }

    private void addCustomVariable() {
        aB dialog = new aB(logicEditor);
        dialog.a(R.drawable.abc_96_color);
        dialog.b("Add a new custom variable");

        LinearLayout root = new LinearLayout(logicEditor);
        root.setOrientation(LinearLayout.VERTICAL);

        TextInputLayout modifierLayout = commonTextInputLayout();
        EditText modifier = commonEditText("private, public or public static (optional)");
        modifierLayout.addView(modifier);
        modifierLayout.setHelperText("Enter modifier e.g. private, public, public static, or empty (package private).");
        modifierLayout.setPadding(0, 0, 0, (int) getDip(8));
        root.addView(modifierLayout);

        TextInputLayout typeLayout = commonTextInputLayout();
        EditText type = commonEditText("Type, e.g. File");
        typeLayout.addView(type);
        root.addView(typeLayout);

        TextInputLayout nameLayout = commonTextInputLayout();
        EditText name = commonEditText("Name, e.g. file");
        nameLayout.addView(name);
        root.addView(nameLayout);

        TextInputLayout initializerLayout = commonTextInputLayout();
        EditText initializer = commonEditText("Initializer, e.g. new File() (optional)");
        initializerLayout.addView(initializer);
        root.addView(initializerLayout);

        ZB validator = new ZB(getContext(), nameLayout, uq.b, uq.a(), projectDataManager.a(projectFile));

        dialog.a(root);
        dialog.b(Helper.getResString(R.string.common_word_add), view -> {
            String variableModifier = modifier.getText().toString();
            variableModifier = isEmpty(variableModifier) ? "" : variableModifier + " ";
            String variableType = type.getText().toString();
            String variableName = name.getText().toString();
            String variableInitializer = initializer.getText().toString();

            boolean validType = !isEmpty(variableType);
            boolean validName = !isEmpty(variableName);
            boolean getsInitialized = !isEmpty(variableInitializer);

            if (validType) {
                typeLayout.setError(null);
            } else {
                if (validName) typeLayout.requestFocus();
                typeLayout.setError("Type can't be empty");
            }

            CharSequence nameError = nameLayout.getError();
            if (nameError == null || "Name can't be empty".contentEquals(nameError)) {
                if (validName) {
                    nameLayout.setError(null);
                } else {
                    nameLayout.requestFocus();
                    nameLayout.setError("Name can't be empty");
                }
            }

            if (validName && validType && validator.b()) {
                String toAdd = variableModifier + variableType + " " + variableName;
                if (getsInitialized) {
                    toAdd += " = " + variableInitializer;
                }
                logicEditor.b(5, toAdd);
                dialog.dismiss();
            }
        });
        dialog.a(Helper.getResString(R.string.common_word_cancel), Helper.getDialogDismissListener(dialog));
        dialog.show();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        modifierLayout.requestFocus();
    }

    private void removeVariable() {
        SketchDialog dialog = new SketchDialog(logicEditor);
        dialog.setTitle(Helper.getResString(R.string.logic_editor_title_remove_variable));
        dialog.setIcon(R.drawable.delete_96);
        View var2 = wB.a(logicEditor, R.layout.property_popup_selector_single);
        ViewGroup viewGroup = var2.findViewById(R.id.rg_content);

        ArrayList<String> bools = getUsedVariable(0);
        for (int i = 0, boolsSize = bools.size(); i < boolsSize; i++) {
            if (i == 0) viewGroup.addView(commonTextView("Boolean (" + boolsSize + ")"));
            viewGroup.addView(commonRadioButton(bools.get(i)));
        }

        ArrayList<String> ints = getUsedVariable(1);
        for (int i = 0, intsSize = ints.size(); i < intsSize; i++) {
            if (i == 0) viewGroup.addView(commonTextView("Number (" + intsSize + ")"));
            viewGroup.addView(commonRadioButton(ints.get(i)));
        }

        ArrayList<String> strs = getUsedVariable(2);
        for (int i = 0, strsSize = strs.size(); i < strsSize; i++) {
            if (i == 0) viewGroup.addView(commonTextView("String (" + strsSize + ")"));
            viewGroup.addView(commonRadioButton(strs.get(i)));
        }

        ArrayList<String> maps = getUsedVariable(3);
        for (int i = 0, mapSize = maps.size(); i < mapSize; i++) {
            if (i == 0) viewGroup.addView(commonTextView("Map (" + mapSize + ")"));
            viewGroup.addView(commonRadioButton(maps.get(i)));
        }

        ArrayList<String> vars = getUsedVariable(5);
        for (int i = 0, varsSize = vars.size(); i < varsSize; i++) {
            if (i == 0) viewGroup.addView(commonTextView("Custom Variable (" + varsSize + ")"));
            viewGroup.addView(commonRadioButton(vars.get(i)));
        }

        dialog.setView(var2);
        dialog.setPositiveButton(Helper.getResString(R.string.common_word_remove), view -> {
            int childCount = viewGroup.getChildCount();
            String eventName = logicEditor.C + "_" + logicEditor.D;
            String javaName = logicEditor.M.getJavaName();

            for (int i = 0; i < childCount; i++) {
                if (viewGroup.getChildAt(i) instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) viewGroup.getChildAt(i);
                    String variable = radioButton.getText().toString();
                    if (radioButton.isChecked()) {
                        if (!logicEditor.o.c(variable)) {
                            if (!projectDataManager.c(javaName, variable, eventName)) {
                                logicEditor.m(variable);
                                dialog.dismiss();
                            }
                        } else {
                            SketchwareUtil.toastError(Helper.getResString(R.string.logic_editor_message_currently_used_variable), bB.TOAST_WARNING);
                            return;
                        }
                    }
                }
            }
            dialog.dismiss();
        });
        dialog.setNegativeButton(Helper.getResString(R.string.common_word_cancel), null);
        dialog.show();
    }

    private void addCustomList() {
        aB dialog = new aB(logicEditor);
        dialog.a(R.drawable.add_96_blue);
        dialog.b("Add a new custom List");

        LinearLayout root = new LinearLayout(logicEditor);
        root.setOrientation(LinearLayout.VERTICAL);

        TextInputLayout typeLayout = commonTextInputLayout();
        EditText type = commonEditText("Type, e.g. ArrayList<Data>");
        typeLayout.addView(type);

        TextInputLayout nameLayout = commonTextInputLayout();
        EditText name = commonEditText("Name, e.g. dataList");
        nameLayout.addView(name);

        root.addView(typeLayout);
        root.addView(nameLayout);

        ZB validator = new ZB(getContext(), nameLayout, uq.b, uq.a(), projectDataManager.a(projectFile));

        dialog.a(root);
        dialog.b(Helper.getResString(R.string.common_word_add), view -> {
            String variableType = type.getText().toString();
            String variableName = name.getText().toString();

            boolean validType = !isEmpty(variableType);
            boolean validName = !isEmpty(variableName);

            if (validType) {
                typeLayout.setError(null);
            } else {
                if (validName) typeLayout.requestFocus();
                typeLayout.setError("Type can't be empty");
            }

            CharSequence nameError = nameLayout.getError();
            if (nameError == null || "Name can't be empty".contentEquals(nameError)) {
                if (validName) {
                    nameLayout.setError(null);
                } else {
                    nameLayout.requestFocus();
                    nameLayout.setError("Name can't be empty");
                }
            }

            if (validType && validName && validator.b()) {
                logicEditor.a(4, variableType + " " + variableName + " = new ArrayList<>()");
                dialog.dismiss();
            }
        });
        dialog.a(Helper.getResString(R.string.common_word_cancel), Helper.getDialogDismissListener(dialog));
        dialog.show();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        typeLayout.requestFocus();
    }

    private void removeList() {
        aB dialog = new aB(logicEditor);
        dialog.b(Helper.getResString(R.string.logic_editor_title_remove_list));
        dialog.a(R.drawable.delete_96);
        View var2 = wB.a(logicEditor, R.layout.property_popup_selector_single);
        ViewGroup viewGroup = var2.findViewById(R.id.rg_content);

        ArrayList<String> listInts = getUsedList(1);
        for (int i = 0, listIntSize = listInts.size(); i < listIntSize; i++) {
            if (i == 0) viewGroup.addView(commonTextView("List Integer (" + listIntSize + ")"));
            viewGroup.addView(commonRadioButton(listInts.get(i)));
        }

        ArrayList<String> listStrs = getUsedList(2);
        for (int i = 0, listStrSize = listStrs.size(); i < listStrSize; i++) {
            if (i == 0) viewGroup.addView(commonTextView("List String (" + listStrSize + ")"));
            viewGroup.addView(commonRadioButton(listStrs.get(i)));
        }

        ArrayList<String> listMaps = getUsedList(3);
        for (int i = 0, listMapSize = listMaps.size(); i < listMapSize; i++) {
            if (i == 0) viewGroup.addView(commonTextView("List Map (" + listMapSize + ")"));
            viewGroup.addView(commonRadioButton(listMaps.get(i)));
        }

        ArrayList<String> listCustom = getUsedList(4);
        for (int i = 0, listCustomSize = listCustom.size(); i < listCustomSize; i++) {
            if (i == 0) viewGroup.addView(commonTextView("List Custom (" + listCustomSize + ")"));
            viewGroup.addView(commonRadioButton(listCustom.get(i)));
        }

        dialog.a(var2);
        dialog.b(Helper.getResString(R.string.common_word_remove), view -> {
            String javaName = logicEditor.M.getJavaName();
            String eventName = logicEditor.C + "_" + logicEditor.D;

            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (viewGroup.getChildAt(i) instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) viewGroup.getChildAt(i);
                    String list = radioButton.getText().toString();

                    if (radioButton.isChecked()) {
                        if (!logicEditor.o.b(list)) {
                            if (!projectDataManager.b(javaName, list, eventName)) {
                                logicEditor.l(list);
                                dialog.dismiss();
                            }
                        } else {
                            SketchwareUtil.toastError(Helper.getResString(R.string.logic_editor_message_currently_used_list), bB.TOAST_WARNING);
                        }
                        return;
                    }
                }
            }
            dialog.dismiss();
        });
        dialog.a(Helper.getResString(R.string.common_word_cancel), Helper.getDialogDismissListener(dialog));
        dialog.show();
    }

    private TextInputLayout commonTextInputLayout() {
        TextInputLayout textInputLayout = new TextInputLayout(logicEditor);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(
                (int) getDip(8),
                0,
                (int) getDip(8),
                0
        );
        textInputLayout.setLayoutParams(layoutParams);
        return textInputLayout;
    }

    private TextView commonTextView(String text) {
        TextView textView = new TextView(logicEditor);
        textView.setText(text);
        textView.setPadding(
                (int) getDip(2),
                (int) getDip(4),
                (int) getDip(4),
                (int) getDip(4)
        );
        textView.setTextSize(14f);
        return textView;
    }

    private EditText commonEditText(String hint) {
        EditText editText = new EditText(logicEditor);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        editText.setPadding(
                (int) getDip(4),
                (int) getDip(8),
                (int) getDip(8),
                (int) getDip(8)
        );
        editText.setTextSize(16f);
        editText.setTextColor(0xff000000);
        editText.setHint(hint);
        editText.setHintTextColor(0xff607d8b);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setPrivateImeOptions("defaultInputmode=english;");
        return editText;
    }

    private RadioButton commonRadioButton(String title) {
        RadioButton radioButton = new RadioButton(logicEditor);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) getDip(40),
                1.0f);
        radioButton.setLayoutParams(layoutParams);
        radioButton.setText(title);
        radioButton.setTextColor(0xff000000);
        radioButton.setTextSize(14f);
        return radioButton;
    }
}
