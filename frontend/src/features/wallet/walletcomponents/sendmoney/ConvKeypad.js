import React from 'react';
import { View, Text, TextInput, Image, ScrollView , TouchableOpacity, StyleSheet } from 'react-native';
import VirtualKeyboard from './VirtualKeypad';
import { SCREEN_HEIGHT, SCREEN_WIDTH } from '../ScreenSize';

const convInput = ['+1만', '+5만', '+10만', '+100만', '전액'];

export const ConvPad = React.memo(function ConvPad({addMoney}){
  // console.log('리로딩')
  const pressConvBtn = (idx)=> {
    let plus = 10000;
    if(idx==3){
      plus = 1000000;
    }else if(idx==4){
      // 전액 다 선택
    }else{
      plus=(idx!==0)?50000*idx:plus;
    }
    addMoney(plus);
  }
  return(
    <>
    <View style={styles.convPad}>{
      convInput.map((conv, index)=>{
        return(
        <TouchableOpacity style={styles.convBtn} key={index} onPress={()=>{pressConvBtn(index)}}>
          <Text style={styles.convFont}>{conv}</Text>
        </TouchableOpacity>)
      })}
    </View>
    <VirtualKeyboard addMoney={addMoney} color='black'/>
    </>
  )
})

const styles = StyleSheet.create({
  convPad:{
    width:'100%',
    height:SCREEN_HEIGHT*0.04,
    flexDirection:'row',
    alignItems:'center',
    justifyContent:'space-around',
  },
  convBtn:{
    width:'15%',
    height:'100%',
    backgroundColor:'#F3F6FB',
    borderRadius: 10,
    justifyContent:'center',
    alignItems:'center'
  },
  convFont:{
    color:'#498AC6',
    fontWeight:'bold',
  }
})