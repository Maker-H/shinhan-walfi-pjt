import React from 'react';
import { 
  View,
  Image,
  TouchableOpacity,
  Text
 } from 'react-native';
 import { GestureHandlerRootView } from 'react-native-gesture-handler';
import JumpAccount from '../../../assets/wallet/JumpAccount.png';
import BaseballAccount from '../../../assets/wallet/BaseballAccount.png';
import LevelupAccount from '../../../assets/wallet/LevelupAccount.png'
import TopTenAccount from '../../../assets/wallet/TopTenAccount.png'
import BattleAccount from '../../../assets/wallet/BattleAccount.png';
import LandAccount from '../../../assets/wallet/LandAccount.png';

import { RFPercentage } from "react-native-responsive-fontsize";
import {Background} from '../walletcomponents/CommonStyle';
import GoFight from '../walletcomponents/GoFight';

import { SCREEN_HEIGHT,SCREEN_WIDTH } from '../walletcomponents/ScreenSize';
import axios from '../../../common/http-common';
import { useSelector } from 'react-redux';
import { ScrollView } from 'react-native-gesture-handler';

export default function MakeAccount({ navigation }){
  const userId = useSelector(state=>state.auth.userId)
  const onPress = async(type) => {
    let response;
    const data = {
      userId
    }
    try{
      if(type==='Levelup'){
        response = await axios.post('character/maxcharacter', data);
      }else if(type==='Top10'){
        response = await axios.post('branch/getmanagernum',data);
      }else if(type==='Battle'){
        response = await axios.post(`battle/getbattlecount?userId=${userId}`)
        console.log(response)
      }else if(type==='Land'){
        response = await axios.post(`branch/getmanagernum`,data)
      }else{
        return;
      }
      navigation.navigate('MakeDeposit',{type, data:response.data.data})
    }catch(err){
      console.log('예금 생성 실패',err)
    }
  }

  //DOM에 때려 박은거 바꾸기 
  return(
    <GestureHandlerRootView style={Background.background} >
      <ScrollView showsVerticalScrollIndicator={false}>
        <View style={{width:'90%',}}>
          <Text style={{fontSize:RFPercentage(3), fontWeight:'bold'}}>정기 예금</Text>
        </View>
        <TouchableOpacity onPress={()=>onPress('Levelup')}>
          <Image source={LevelupAccount} style={{width:SCREEN_WIDTH*0.9, height:SCREEN_HEIGHT*0.25, resizeMode:'contain'}}/>
        </TouchableOpacity>
        <TouchableOpacity onPress={()=>onPress('Top10')}>
          <Image source={TopTenAccount} style={{width:SCREEN_WIDTH*0.9, height:SCREEN_HEIGHT*0.25, resizeMode:'contain'}}/>
        </TouchableOpacity>
        <TouchableOpacity onPress={()=>onPress('Battle')}>
          <Image source={BattleAccount} style={{width:SCREEN_WIDTH*0.9, height:SCREEN_HEIGHT*0.25, resizeMode:'contain'}}/>
        </TouchableOpacity>
        <TouchableOpacity onPress={()=>onPress('Land')}>
          <Image source={LandAccount} style={{width:SCREEN_WIDTH*0.9, height:SCREEN_HEIGHT*0.25, resizeMode:'contain'}}/>
        </TouchableOpacity>
        <View style={{width:'90%',}}>
          <Text style={{fontSize:RFPercentage(3), fontWeight:'bold'}}>적금</Text>
        </View>
        <TouchableOpacity onPress={()=>navigation.navigate('WalletHome')}>
          <Image source={JumpAccount} style={{width:SCREEN_WIDTH*0.9, height:SCREEN_HEIGHT*0.25, resizeMode:'contain'}}/>
        </TouchableOpacity>
        <TouchableOpacity onPress={()=>navigation.navigate('WalletHome')}>
          <Image source={BaseballAccount} style={{width:SCREEN_WIDTH*0.9, height:SCREEN_HEIGHT*0.25, resizeMode:'contain'}}/>
        </TouchableOpacity>
      </ScrollView>
      <GoFight/>
    </GestureHandlerRootView>
  )
}
