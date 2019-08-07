//
// Created by tiger on 18/4/27.
//
#include "com_sina_shopguide_util_SecretParams.h"
#include "Md5.h"
#include <string.h>
#include<stdio.h>
JNIEXPORT jstring JNICALL Java_com_sina_shopguide_util_SecretParams_getHttpTransMd5Key
  (JNIEnv * env, jclass claz, jstring jstr){
    char * rtn = NULL;
    jclass clsstring = env->FindClass("java/lang/String");
    jstring strencode = env->NewStringUTF("utf-8");
    jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray barr= (jbyteArray)env->CallObjectMethod(jstr,mid,strencode);
    jsize alen = env->GetArrayLength(barr);
    jbyte * ba = env->GetByteArrayElements(barr,JNI_FALSE);
    if(alen > 0)
    {
        rtn = (char*)malloc(alen + 21); //new char[alen+1];
        rtn[alen + 20]='\0';
        memcpy(rtn,ba,alen);
        rtn[alen]=0;
    }
    env->ReleaseByteArrayElements(barr,ba,0);
    strcat(rtn,"28ce75531469b56f4bc8");

    unsigned char decrypt[16] = { 0 };
    MD5_CTX md5;

    MD5Init(&md5);
    MD5Update(&md5, (unsigned char *)rtn, (unsigned int )strlen((const char *)rtn));
    MD5Final(&md5, decrypt);

    int i;
    char destination[33]={0};
    for (i = 0; i < 16; i++) {
        sprintf(destination, "%s%02x", destination, decrypt[i]);
    }

    //释放所有资源
    if(rtn != NULL)
        free(rtn);

    env->DeleteLocalRef(strencode);
    env->DeleteLocalRef(clsstring);

    return (env)->NewStringUTF(destination);
  }

/*
 * Class:     com_sina_shopguide_util_SecretParams
 * Method:    getHttpTransSource
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_sina_shopguide_util_SecretParams_getHttpTransSource
  (JNIEnv * env, jclass claz){
  return (env)->NewStringUTF("1002");

  }

