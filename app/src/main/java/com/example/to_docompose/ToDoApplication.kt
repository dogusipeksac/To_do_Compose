package com.example.to_docompose

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
//@AndroidEntryPoint → Cannot create an instance of clas veya has no zero argument constructor hatası alıyorsanız bağımlılıkların inject (oluşturulacağını ) edileceğini belirtmediğinizden kaynaklıdır.
//@Module → Hilt module’ü olacağını belirtiyoruz.
//@InstallIn → Kullanacağımız kapsamı belirtiyoruz
//@Provides → Module içerisinde provides ile tanımlanan fonksiyonları her zaman kullanacaktır.
//@Singleton → Nesneye her ulaştığımızda hep aynı nesneyi verecektir bize sürekli yeniden create etmez .
//@HiltAndroidApp → Gerekli bağımlılıkları oluşturmak için Hilt’i tetikler.
@HiltAndroidApp
class ToDoApplication : Application()