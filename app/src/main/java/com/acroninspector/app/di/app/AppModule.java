package com.acroninspector.app.di.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Room;

import com.acroninspector.app.BuildConfig;
import com.acroninspector.app.common.constants.NetworkConstants;
import com.acroninspector.app.common.constants.PreferencesConstants;
import com.acroninspector.app.common.utils.SchedulersProvider;
import com.acroninspector.app.data.datasource.database.AppDatabase;
import com.acroninspector.app.data.repositories.PreferencesRepositoryImpl;
import com.acroninspector.app.di.activity.main.MainComponent;
import com.acroninspector.app.di.fragment.comment.CommentComponent;
import com.acroninspector.app.di.fragment.controlprocedure.ControlProcedureComponent;
import com.acroninspector.app.di.fragment.defectcause.DefectCauseComponent;
import com.acroninspector.app.di.fragment.defectdetails.DefectDetailsComponent;
import com.acroninspector.app.di.fragment.defectname.DefectNameComponent;
import com.acroninspector.app.di.fragment.defects.defects.DefectsComponent;
import com.acroninspector.app.di.fragment.defects.searchresult.DefectsSearchResultComponent;
import com.acroninspector.app.di.fragment.login.annotations.HistoryOfAnnotationsComponent;
import com.acroninspector.app.di.fragment.login.division.DivisionComponent;
import com.acroninspector.app.di.fragment.edittask.EditTaskComponent;
import com.acroninspector.app.di.fragment.equipments.nested.NestedEquipmentComponent;
import com.acroninspector.app.di.fragment.equipments.root.RootEquipmentComponent;
import com.acroninspector.app.di.fragment.login.auth.LoginComponent;
import com.acroninspector.app.di.fragment.mediafiles.MediaFilesComponent;
import com.acroninspector.app.di.fragment.nfc.definenfc.DefineNfcComponent;
import com.acroninspector.app.di.fragment.nfc.nfcname.NfcNameComponent;
import com.acroninspector.app.di.fragment.notifications.readed.ReadedNotificationsComponent;
import com.acroninspector.app.di.fragment.notifications.unreaded.NewNotificationsComponent;
import com.acroninspector.app.di.fragment.questions.QuestionsComponent;
import com.acroninspector.app.di.fragment.registerdefect.RegisterDefectComponent;
import com.acroninspector.app.di.fragment.search.SearchComponent;
import com.acroninspector.app.di.fragment.taskcomment.TaskCommentComponent;
import com.acroninspector.app.di.fragment.taskdetails.bypass.TaskDetailsByPassComponent;
import com.acroninspector.app.di.fragment.taskdetails.bypassmanagement.TaskDetailsByPassManagementComponent;
import com.acroninspector.app.di.fragment.tasks.completed.CompletedTasksComponent;
import com.acroninspector.app.di.fragment.tasks.inprogress.InProgressTasksComponent;
import com.acroninspector.app.di.fragment.login.userfunction.UserFunctionComponent;
import com.acroninspector.app.di.global.base.BaseComponentBuilder;
import com.acroninspector.app.di.global.scope.PerApplication;
import com.acroninspector.app.domain.repositories.PreferencesRepository;
import com.acroninspector.app.presentation.activity.main.MainActivity;
import com.acroninspector.app.presentation.fragment.annotations.HistoryOfAnnotationsFragment;
import com.acroninspector.app.presentation.fragment.comment.CommentFragment;
import com.acroninspector.app.presentation.fragment.controlprocedure.ControlProcedureFragment;
import com.acroninspector.app.presentation.fragment.defectdetails.DefectDetailsFragment;
import com.acroninspector.app.presentation.fragment.defectparameters.defectcause.DefectCauseFragment;
import com.acroninspector.app.presentation.fragment.defectparameters.defectname.DefectNameFragment;
import com.acroninspector.app.presentation.fragment.defects.defectlogs.DefectLogsFragment;
import com.acroninspector.app.presentation.fragment.defects.searchresult.DefectsSearchResultFragment;
import com.acroninspector.app.presentation.fragment.edittask.EditTaskFragment;
import com.acroninspector.app.presentation.fragment.equipments.nested.NestedEquipmentFragment;
import com.acroninspector.app.presentation.fragment.equipments.root.RootEquipmentFragment;
import com.acroninspector.app.presentation.fragment.login.auth.LoginFragment;
import com.acroninspector.app.presentation.fragment.mediafiles.MediaFilesFragment;
import com.acroninspector.app.presentation.fragment.nfc.definenfc.DefineNfcFragment;
import com.acroninspector.app.presentation.fragment.nfc.nfcname.NfcNameFragment;
import com.acroninspector.app.presentation.fragment.notifications.readed.ReadedNotificationsFragment;
import com.acroninspector.app.presentation.fragment.notifications.unreaded.NewNotificationsFragment;
import com.acroninspector.app.presentation.fragment.questions.QuestionsFragment;
import com.acroninspector.app.presentation.fragment.registerdefect.RegisterDefectFragment;
import com.acroninspector.app.presentation.fragment.search.SearchFragment;
import com.acroninspector.app.presentation.fragment.login.supervisedunit.SupervisedUnitFragment;
import com.acroninspector.app.presentation.fragment.taskcomment.TaskCommentFragment;
import com.acroninspector.app.presentation.fragment.taskdetails.bypass.TaskDetailsByPassFragment;
import com.acroninspector.app.presentation.fragment.taskdetails.bypassmanagement.TaskDetailsByPassManagementFragment;
import com.acroninspector.app.presentation.fragment.tasks.completed.CompletedTasksFragment;
import com.acroninspector.app.presentation.fragment.tasks.inprogress.InProgressTasksFragment;
import com.acroninspector.app.presentation.fragment.login.userfunction.UserFunctionFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.acroninspector.app.common.constants.DatabaseConstants.DATABASE_NAME;

@Module(subcomponents = {
        MediaFilesComponent.class, QuestionsComponent.class, TaskCommentComponent.class,
        TaskDetailsByPassComponent.class, DefectsComponent.class, NestedEquipmentComponent.class,
        UserFunctionComponent.class, HistoryOfAnnotationsComponent.class, LoginComponent.class, DivisionComponent.class,
        MainComponent.class, DefectCauseComponent.class,
        InProgressTasksComponent.class, CompletedTasksComponent.class, CommentComponent.class,
        RegisterDefectComponent.class, RootEquipmentComponent.class, SearchComponent.class,
        DefectsSearchResultComponent.class, DefectNameComponent.class, NewNotificationsComponent.class,
        ReadedNotificationsComponent.class, TaskDetailsByPassManagementComponent.class,
        ControlProcedureComponent.class, EditTaskComponent.class, NfcNameComponent.class,
        DefineNfcComponent.class, DefectDetailsComponent.class
})
public class AppModule {

    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @PerApplication
    @Provides
    Context provideApplicationContext() {
        return context;
    }

    @PerApplication
    @Provides
    SchedulersProvider provideShedulersProvider() {
        return new SchedulersProvider();
    }

    @PerApplication
    @Provides
    SharedPreferences provideSharedPreferences() {
        return context.getSharedPreferences(PreferencesConstants.APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    @PerApplication
    @Provides
    PreferencesRepository providePreferencesRepository() {
        return new PreferencesRepositoryImpl(context);
    }

    @PerApplication
    @Provides
    AppDatabase provideDatabase() {
        return Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    @PerApplication
    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .build();
    }

    @PerApplication
    @Provides
    Gson provideGson() {
        return new GsonBuilder().setLenient().create();
    }

    @PerApplication
    @Provides
    OkHttpClient provideOkHttpClient(
            HttpLoggingInterceptor interceptor,
            TrustManager[] trustAllCerts,
            SSLSocketFactory sslSocketFactory
    ) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier((hostname, session) -> true)
                .connectTimeout(NetworkConstants.TIME_OUT_MINUTES, TimeUnit.MINUTES)
                .writeTimeout(NetworkConstants.TIME_OUT_MINUTES, TimeUnit.MINUTES)
                .readTimeout(NetworkConstants.TIME_OUT_MINUTES, TimeUnit.MINUTES)
                .build();
    }

    @PerApplication
    @Provides
    SSLSocketFactory provideSSLSocketFactory(TrustManager[] trustAllCerts) {
        try {
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PerApplication
    @Provides
    @SuppressLint("TrustAllX509TrustManager")
    TrustManager[] provideTrustManager() {
        return new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
                //Do nothing
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
                //Do nothing
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        }};
    }

    @PerApplication
    @Provides
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return interceptor;
    }

    @Provides
    @IntoMap
    @ClassKey(MediaFilesFragment.class)
    BaseComponentBuilder provideAttachmentsActivity(MediaFilesComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(MainActivity.class)
    BaseComponentBuilder provideMainActivity(MainComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(TaskDetailsByPassFragment.class)
    BaseComponentBuilder provideTaskByPassDetailsFragment(TaskDetailsByPassComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(TaskDetailsByPassManagementFragment.class)
    BaseComponentBuilder provideTaskDetailsByPassManagementFragment(TaskDetailsByPassManagementComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(ControlProcedureFragment.class)
    BaseComponentBuilder provideControlProcedureFragment(ControlProcedureComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(DefineNfcFragment.class)
    BaseComponentBuilder provideDefineNfcFragment(DefineNfcComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(NfcNameFragment.class)
    BaseComponentBuilder provideNfcNameFragment(NfcNameComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(EditTaskFragment.class)
    BaseComponentBuilder provideEditTaskFragment(EditTaskComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(DefectLogsFragment.class)
    BaseComponentBuilder provideDefectsFragment(DefectsComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(DefectsSearchResultFragment.class)
    BaseComponentBuilder provideDefectsSearchResultFragment(DefectsSearchResultComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(NestedEquipmentFragment.class)
    BaseComponentBuilder provideNestedEquipmentsFragment(NestedEquipmentComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(RootEquipmentFragment.class)
    BaseComponentBuilder provideRootEquipmentsFragment(RootEquipmentComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(UserFunctionFragment.class)
    BaseComponentBuilder provideUserFunctionFragment(UserFunctionComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(HistoryOfAnnotationsFragment.class)
    BaseComponentBuilder provideHistoryOfAnnotationsFragment(HistoryOfAnnotationsComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(LoginFragment.class)
    BaseComponentBuilder provideLoginFragment(LoginComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(SupervisedUnitFragment.class)
    BaseComponentBuilder provideDivisionFragment(DivisionComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(QuestionsFragment.class)
    BaseComponentBuilder provideQuestionsFragment(QuestionsComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(InProgressTasksFragment.class)
    BaseComponentBuilder provideInProgressTasksFragment(InProgressTasksComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(CompletedTasksFragment.class)
    BaseComponentBuilder provideCompletedTasksFragment(CompletedTasksComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(NewNotificationsFragment.class)
    BaseComponentBuilder provideNewNotificationsFragment(NewNotificationsComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(ReadedNotificationsFragment.class)
    BaseComponentBuilder provideReadedNotificationsFragment(ReadedNotificationsComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(CommentFragment.class)
    BaseComponentBuilder provideCommentFragment(CommentComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(DefectCauseFragment.class)
    BaseComponentBuilder provideDefectCauseFragment(DefectCauseComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(DefectNameFragment.class)
    BaseComponentBuilder provideDefectNameFragment(DefectNameComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(DefectDetailsFragment.class)
    BaseComponentBuilder provideDefectDetailsFragment(DefectDetailsComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(TaskCommentFragment.class)
    BaseComponentBuilder provideTaskCommentFragment(TaskCommentComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(RegisterDefectFragment.class)
    BaseComponentBuilder provideRegisterDefectFragment(RegisterDefectComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(SearchFragment.class)
    BaseComponentBuilder provideSearchFragment(SearchComponent.Builder builder) {
        return builder;
    }
}
