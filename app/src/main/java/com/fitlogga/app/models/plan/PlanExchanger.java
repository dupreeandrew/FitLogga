package com.fitlogga.app.models.plan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.fitlogga.app.R;
import com.fitlogga.app.activities.PlanCreatorActivity;
import com.fitlogga.app.models.ApplicationContext;
import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.Quota;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.ExerciseTranslator;
import com.fitlogga.app.utils.GsonHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * PlanExchanger is a class used primarily to make plans shareable by other users.
 * Not much validation is done here, because it's all handled through the backend.
 */
public class PlanExchanger {

    public static class Plan implements PlanSource {
        private PlanSummary planSummary;
        private EnumMap<Day, DailyRoutine> dailyRoutines;

        public Plan(PlanSummary planSummary, EnumMap<Day, DailyRoutine> dailyRoutines) {
            this.planSummary = planSummary;
            this.dailyRoutines = dailyRoutines;
        }

        @Override
        public PlanSummary getPlanSummary() {
            return planSummary;
        }

        @Override
        public EnumMap<Day, DailyRoutine> getDailyRoutines() {
            return dailyRoutines;
        }
    }

    private interface RequestListener {
        void onSuccess(String content);
        void onFail();
    }

    public static abstract class DialogListener {
        private boolean isAborted = false;

        boolean isAborted() {
            return isAborted;
        }

        public void abortTask() {
            this.isAborted = true;
        }
    }

    public static abstract class ImportDialogListener extends DialogListener {
        public abstract void onSuccess();
        public abstract void onFail(String localizedErrorMessage);
    }

    public static abstract class ExportDialogListener extends DialogListener {
        public abstract void onSuccess(String planCode);
        public abstract void onFail(String localizedErrorMessage);
    }


    private static final String EXCHANGE_SERVER_UPLOAD_URL = "https://fitlogga.com/exchange-server/upload.php";
    private static final String EXCHANGE_SERVER_POST_UPLOAD_PARAM = "payload";
    private static final String EXCHANGE_SERVER_GET_KEY_URL = "https://fitlogga.com/exchange-server/download.php?key=";
    private static final String EXCHANGE_SERVER_ERROR_QUOTA_EXCEEDED = "quota-exceeded";

    private static final String DELIMITER = "!%@%@%!";

    private static final String QUOTA_NAME = "plan_export";
    private static final int QUOTA_MAX_EXPORTS_PER_HOUR = 15;


    /**
     * @param listener This will contain the plan id key.
     */
    public static void exportPlan(String planName, ExportDialogListener listener) {

        if (!exportQuotaIsOKAY()) {
            String error = getString(R.string.plan_exchange_error_export_please_wait);
            listener.onFail(error);


            return;
        }

        PlanReader planReader = PlanReader.attachTo(planName);
        if (planReader == null) {
            listener.onFail("Plan does not exist"); // should not be called.
            return;
        }

        PlanSummary planSummary = planReader.getPlanSummary();
        EnumMap<Day, DailyRoutine> dailyRoutines = planReader.getDailyRoutines();

        for (Map.Entry<Day, DailyRoutine> entry : dailyRoutines.entrySet()) {
            DailyRoutine.Exercises exercises = entry.getValue().getExercises();
            if (exercises.isEmpty()) {
                Day day = entry.getKey();
                dailyRoutines.remove(day);
            }
        }

        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        String planSummaryJson = gson.toJson(planSummary);
        String dailyRoutinesJson = gson.toJson(dailyRoutines);
        String planPayload = planSummaryJson + DELIMITER + dailyRoutinesJson;

        Log.d("testyy-", dailyRoutinesJson);

        exportPlanToWebServer(planPayload, listener);

    }

    private static boolean exportQuotaIsOKAY() {
        Quota quota = Quota.get(QUOTA_NAME);

        if (quota.getMillisSinceLastReset() > 3600000)  { // 1 hour
            quota.resetQuota();
            return true;
        }

        if (quota.getNumUses() < QUOTA_MAX_EXPORTS_PER_HOUR) {
            return true;
        }

        return false;

    }

    private static void exportPlanToWebServer(String planPayload, ExportDialogListener listener) {
        OkHttpClient client = new OkHttpClient();
        Request uploadRequest = getUploadRequest(planPayload);
        Callback callback = getFinishedDownloadingCallback(new RequestListener() {
            @Override
            public void onSuccess(String content) {

                if (content.equals(EXCHANGE_SERVER_ERROR_QUOTA_EXCEEDED)) {
                    String error = getString(R.string.plan_exchange_error_export_please_wait);
                    listener.onFail(error);
                    return;
                }

                Quota.get(QUOTA_NAME).addUse();
                listener.onSuccess(content);
            }

            @Override
            public void onFail() {
                String msg = getString(R.string.plan_exchange_error_could_not_retrieve_plan);
                listener.onFail(msg);
            }
        });
        client.newCall(uploadRequest).enqueue(callback);
    }

    private static Request getUploadRequest(String planPayload) {
        RequestBody formBody = new FormBody.Builder()
                .add(EXCHANGE_SERVER_POST_UPLOAD_PARAM, planPayload)
                .build();
        return new Request.Builder()
                .url(EXCHANGE_SERVER_UPLOAD_URL)
                .post(formBody)
                .build();
    }

    /**
     * On failure, execute listener#onFail();
     * On success, execute listener#onSuccess(source code of website);
     */
    private static Callback getFinishedDownloadingCallback(RequestListener listener) {
        Handler handler = new Handler(Looper.getMainLooper());
        return new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.post(listener::onFail);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.body() != null) {
                    try {
                        String planId = response.body().string();
                        handler.post(() -> listener.onSuccess(planId));
                    } catch (IOException e) {
                        handler.post(listener::onFail);
                    }

                }
                else {
                    handler.post(listener::onFail);
                }
            }
        };
    }

    public static void openImportPlanDialog(Activity activity, String planId, ImportDialogListener listener) {
        getPlanJson(planId, new RequestListener() {
            @Override
            public void onSuccess(String exportPlanPayload) {

                if (listener.isAborted()) {
                    return;
                }

                if (TextUtils.isEmpty(exportPlanPayload)) {
                    String msg = getString(R.string.plan_exchange_error_invalid_plan);
                    listener.onFail(msg);
                    return;
                }

                listener.onSuccess();
                Intent intent = new Intent(activity, PlanCreatorActivity.class);
                intent.putExtra(PlanCreatorActivity.PREFILLED_EXPORT_PLAN_PAYLOAD, exportPlanPayload);
                activity.startActivity(intent);

            }

            @Override
            public void onFail() {

                if (listener.isAborted()) {
                    return;
                }

                String msg = getString(R.string.plan_exchange_error_could_not_retrieve_plan);
                listener.onFail(msg);
            }
        });
    }

    private static String getString(@StringRes int stringId) {
        Context context = ApplicationContext.getInstance();
        return context.getString(stringId);
    }

    private static void getPlanJson(String planId, RequestListener listener) {
        OkHttpClient client = new OkHttpClient();
        Request request = getDownloadRequest(planId);
        Callback callback = getFinishedDownloadingCallback(listener);
        client.newCall(request).enqueue(callback);
    }

    private static Request getDownloadRequest(String planId) {
        return new Request.Builder()
                .url(EXCHANGE_SERVER_GET_KEY_URL + planId)
                .build();
    }

    public static Plan convertExportPlanPayloadToPlan(String exportPayload) {

        final int PLAN_SUMMARY_INDEX = 0;
        final int DAILY_ROUTINE_MAP_INDEX = 1;

        String[] planPieces = exportPayload.split(DELIMITER);
        String planSummaryJson = planPieces[PLAN_SUMMARY_INDEX];
        String dailyRoutinesMapJson = planPieces[DAILY_ROUTINE_MAP_INDEX];

        PlanSummary planSummary = new Gson().fromJson(planSummaryJson, PlanSummary.class);

        Map<String, Object> rawDailyRoutineMap = GsonHelper.getMapFromJson(dailyRoutinesMapJson);
        EnumMap<Day, DailyRoutine> dailyRoutineMap = new EnumMap<>(Day.class);

        /*
        entry.key = Day # as string
        entry.value = DailyRoutine, written as a map
         */
        for (Map.Entry<String, Object> entry : rawDailyRoutineMap.entrySet()) {
            int dayValue = Integer.parseInt(entry.getKey());
            Day day = Day.fromValue(dayValue);

            DailyRoutine dailyRoutine = new DailyRoutine();

            Map<String, Object> dailyRoutineDetails
                    = (Map<String, Object>) entry.getValue();

            List<Map<String, Object>> rawExerciseList
                    = (List<Map<String, Object>>) dailyRoutineDetails.get("exercises");

            for (Map<String, Object> rawExercise : rawExerciseList) {
                Exercise exercise = ExerciseTranslator.toExercise(rawExercise);
                dailyRoutine.getExercises().add(exercise);
            }

            String name = (String) dailyRoutineDetails.get("name");

            dailyRoutine.setName(name);

            dailyRoutineMap.put(day, dailyRoutine);



        }

        return new Plan(planSummary, dailyRoutineMap);



    }

}
