import android.content.Context
import com.example.l_teach_app_test.Data.Remote.ApiService
import com.example.l_teach_app_test.Data.Repository.AuthRepositoryImpl
import com.example.l_teach_app_test.Data.Repository.PostRepositoryImpl
import com.example.l_teach_app_test.Domain.Repository.AuthRepository
import com.example.l_teach_app_test.Domain.Repository.PostRepository
import com.example.l_teach_app_test.Domain.UseCase.GetPhoneMaskUseCase
import com.example.l_teach_app_test.Domain.UseCase.GetPostsUseCase
import com.example.l_teach_app_test.Domain.UseCase.GetSavedCredentialsUseCase
import com.example.l_teach_app_test.Domain.UseCase.LoginUseCase
import com.example.l_teach_app_test.Presentation.ViewModel.HomeViewModel
import com.example.l_teach_app_test.Presentation.ViewModel.LoginViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("http://dev-exam.l-tech.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }).build()
            )
            .build()
            .create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single<AuthRepository> {
        AuthRepositoryImpl(get(), androidContext().getSharedPreferences("auth", Context.MODE_PRIVATE))
    }
    single<PostRepository> { PostRepositoryImpl(get()) }
}

val useCaseModule = module {
    factory { GetPhoneMaskUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { GetSavedCredentialsUseCase(get()) }
    factory { GetPostsUseCase(get()) }
}

val viewModelModule = module {
    viewModel {
        LoginViewModel(get(), get(), get())
    }
    viewModel { HomeViewModel(get()) }
}
