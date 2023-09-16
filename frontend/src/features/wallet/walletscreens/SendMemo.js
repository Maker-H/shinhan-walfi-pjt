import React from "react";
import { View, Text, Image, TouchableOpacity, StyleSheet } from "react-native";
import { RFPercentage } from "react-native-responsive-fontsize";

import SendCheck from '../../../assets/wallet/SendCheck.png';
import { ButtonStyle } from "../walletcomponents/CommonStyle";
import { convStyle } from "../walletcomponents/virtualkeyboard/ConvKeypad";
import { Background } from "../walletcomponents/CommonStyle";

export default function SendMemo({route,navigation}) {
  // 뒤로가기 막기
  React.useEffect(() =>
    navigation.addListener('beforeRemove', (e) => {
      if(e.data.action.type==='GO_BACK'){
        e.preventDefault();
      }
    }),[navigation]
  );

  const type = route.params.type

  
  return (
    <View style={{...Background.whiteback, justifyContent:'space-evenly'}}>
      <View style={{alignItems:'center'}}>
        <Image source={require('../../../assets/wallet/SendCheck.png')}/>
        <Text style={styles.infoTxt}>{route.params.toBank}{route.params.toAccount}{route.params.toNation}</Text>
      </View>
      <Text style={styles.infoTxt}>{route.params.formMoney}{route.params.outISO} {'\n'}{type}했어요</Text>
      <TouchableOpacity style={{...convStyle.convBtn, height:'6%', width:'30%'}}>
        <Text style={{...convStyle.convFont, fontWeight:'bold', fontSize:RFPercentage(2)}} onPress={()=>navigation.navigate('SendWho',{type, data:route.params.outAcc})}>추가 이체</Text> 
      </TouchableOpacity>
      <TouchableOpacity style={{...ButtonStyle.button, height:'8%'}} onPress={()=>navigation.navigate('WalletHome')}>
        <Text style={ButtonStyle.btnFont}>확인</Text> 
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  infoTxt:{
    fontSize:RFPercentage(3),
    fontWeight:'bold'
  }

})