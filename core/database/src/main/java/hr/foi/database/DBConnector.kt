package hr.foi.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBConnector {
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://vpajdaofzzfhydswujxq.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZwYWpkYW9menpmaHlkc3d1anhxIiwicm9sZSI6ImFub24iLCJpYXQiOjE2OTkwMTU5MzMsImV4cCI6MjAxNDU5MTkzM30.mAoisWK9Cf0I9nCINTFNtMf__okd8npz6ANlyjCW-ko",
        )
        {
            install(Postgrest)
        }
    }

    @Provides
    @Singleton
    fun providesUserDataSource(client: SupabaseClient): UserDataSource {
        return UserDataSource(client)
    }
    @Provides
    @Singleton
    fun provideDataRepository(dataSource: UserDataSource): DataRepository{
        return DefaultDataRepository(dataSource)
    }

}