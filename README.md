# [List] Android MVVM Pattern
> 간단한 MVVM을 사용한 List 화면 Android 예제 입니다. 


* RxJava Version
  * [Branch](https://github.com/haejung83/android_mvvm_list_kotlin/tree/mvvm_rxjava)

* 사용기술

  * Android MVVM
  * DataBinding
  * LiveData
  * Kotlin

  

* Data Server

  * 간단한 데이터 서버로부터 RESTful API를 사용하여 표시할 정보를 획득 합니다.

  * ```shell
    $ git clone https://github.com/haejung83/server_drone_kotlin.git
    $ cd server_drone_kotlin
    $ ./gradlew bootRun
    ```

  

* 화면

  * 에러 또는 결과 없음 (서버가 동작 하지 않거나 네트워크가 불안정한 경우)

  ![00_error](https://user-images.githubusercontent.com/6600546/56936212-16b45f80-6b31-11e9-91e3-3f4d4fe7004a.png)

  * 리스트 표시 (서버에서 데이터를 수신 받아 리스트에 데이터를 표시)

  ![01_list](https://user-images.githubusercontent.com/6600546/56936215-1b791380-6b31-11e9-9e6e-47df9cfa2f49.png)

  * 상세 내용 표시 (리스트에서 선택한 아이템의 상세 화면 표시)

  ![02_detail](https://user-images.githubusercontent.com/6600546/56936217-1fa53100-6b31-11e9-9f82-1dc14618e33b.png)



* MVVM

  * Model (MVP예제에 사용 부분 동일)

    * 데이터 소스 인터페이스 (Repository, Local/Remote Data Source의 인터페이스)

      ```kotlin
      interface DronesDataSource {
          interface LoadDronesCallback {
              fun onDronesLoaded(drones: List<Drone>)
              fun onDataNotAvailable()
          }
          
          interface GetDroneCallback {
              fun onDroneLoaded(drone: Drone)
              fun onDataNotAvailable()
          }
          
          fun getDrones(callback: LoadDronesCallback)
          fun getDrone(name: String, callback: GetDroneCallback)
          fun saveDrone(drone: Drone)
          fun refreshDrones()
          fun deleteAllDrones()
          fun deleteDrone(name: String)
      }
      ```

    * 엔티티 (DTO와 Entity를 겸용으로 쓰는 데이터 객체)

      ```kotlin
      data class Drone @JvmOverloads constructor(
          @ColumnInfo(name = "name") var name: String = "",
          @ColumnInfo(name = "type") var type: String = "",
          @ColumnInfo(name = "prop_size") var size: Int = 0,
          @ColumnInfo(name = "fc") var fc: String = "",
          @ColumnInfo(name = "image") var image: String = "",
          @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
      )
      ```

      

  * MVVM

    * ViewModel
      * App의 Lifecycle의 연계된 UI 데이터의 저장과 관리를 담당하도록 설계되었다.
      * ViewModel은 ViewModel을 획득할 때 ViewModelProvider에게 전달된 Lifecycle의 의하여 관찰(Scoped)된다.
      * ![viewmodel_lifecycle](https://developer.android.com/images/topic/libraries/architecture/viewmodel-lifecycle.png)
    * LiveData
      * LiveData는 Observable 데이터 홀더 클래스이다.
      * LiveData는 lifecycle-aware하다. Activity, Fragment와 같은 다른 App Component의 Lifecycle을 준수한다는 의미이다.
 
      
  * 간단한 개발 순서
  
    1. ViewModel을 만든다.
    2. fragment_drones.xml에 아래처럼 ViewModel을 참조하게 한다.
    
    ```xml
    <Layout>
      <Data>
        <Variable name="droneViewModel" type="..ViewModel.." />
      </Data>
      <FrameLayout>
        <TextView
          ..
          ..
          android:text="@{droneViewModel.name}"
          .. />
      </FrameLayout>
    </Layout>
    ```
    
    3. 프로젝트 리빌드
    4. 생성된 FragmentDronesBinding 클래스를 사용하여 Layout과 Logic을 바인딩한다.
    
    
