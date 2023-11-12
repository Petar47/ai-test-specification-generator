package hr.foi.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ViewModelModule {
    @Provides
    fun providesUserViewModule(repo: DefaultDataRepository): DataViewModel{
        return DataViewModel(repo)
    }

}