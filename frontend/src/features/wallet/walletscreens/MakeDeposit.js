import React, {useState, useEffect} from "react";
import { 
  View,
  TouchableOpacity,
  Text,
  StyleSheet,
  TextInput
 } from "react-native";
import { RFPercentage } from "react-native-responsive-fontsize";
import SelectDropdown from "react-native-select-dropdown";
import axios from "../../../common/http-common";
import { Background, ButtonStyle } from "../walletcomponents/CommonStyle";

import { SCREEN_HEIGHT } from "./WalletHome";
import { useSelector } from "react-redux";
import { useFocusEffect } from "@react-navigation/native";

const Deposit = {
  'Levelup':{
    'message': '최고 레벨 캐릭터 수 +', 
  },
  'Top10':{
    'message': '랭킹 + '
  }
}

// 여기 따로 export 시켰어야 하는데..
const ISO = {
  'KRW': '원',
  'USD': '$',
  'EUR': '€',
  'JPY': '¥',
  'CNY': '¥',
  'AUD': 'AU$' 
}

export default function MakeDetail({route,navigation}){
  
  useFocusEffect(()=>{
    const date = new Date().toDateString();
    console.log(date)
  })
  const {userId, mainAccount} = useSelector(state=> state.auth)
  const [ntnCode, setNtnCode] = useState('KRW');
  const [money, setMoney] = useState('0');  

  const type = route.params.type;
  const detail = route.params.data;

  const selNations = type==='Top10'?['KRW']:['KRW','USD','JPY','EUR','CNY','AUD'];

  const total = Number(money) * Number(detail.가입기간)
  const show = Number(money).toLocaleString('en-US')

  const onPress = () => {
    const data={
      mainAccountNum: mainAccount,
      userId:userId,
      금리:detail.총금리,
      만기일: '2023',
      상품명: detail.상품명,
      입금금액: Number(money),
      통화코드: ntnCode,
    }
    console.log(data)
    const response = axios.post('product/create', data)
    .then(res=>navigation.navigate('WalletHome'));
  }
  return(
    <View style={{...Background.background, padding:'10%', justifyContent:'none'}}>
      <View style={styles.titleContent}>
        <Text style={styles.nowRate}>{`현재 적용 금리 ${detail.총금리}%`}</Text>
        <Text style={styles.rateBg}>{`연 ${detail.기본금리}%`}</Text>
        <Text style={styles.rateBg}>{`${Deposit[type]['message']} ${detail.추가금리}%`}</Text>
      </View>
      <View style={styles.middleContainer}>
        <Text style={{ alignSelf: 'flex-end', marginBottom:'5%', fontWeight:'bold'}}>{`가입기간 ${detail.가입기간}개월`}</Text>
        <View style={styles.inputContainer}>
          <SelectDropdown
            data={selNations}
            onSelect={(selectedItem, index)=>{
              console.log(selectedItem, index)
              setNtnCode(selectedItem)
            }}
            buttonTextAfterSelection={(selectedItem, index)=>{
              return selectedItem
            }}
            defaultValue={selNations[0]}
            buttonStyle={styles.nationSel}
            dropdownIconPosition={'left'}
          />
          <TextInput keyboardType="number-pad" placeholder="금액 입력" style={styles.txtInput} value={show} onChangeText={i => setMoney(i.replace(/,/g,''))}/>
        </View>
        <View style={{width:'100%'}}>
          <Text style={styles.rateCalc}>만기 예상 원금</Text>
          <Text style={{...styles.rateCalc, marginBottom:'3%'}}>{(total).toLocaleString('en-US')} {ISO[ntnCode]}</Text>
          <Text style={styles.rateCalc}>만기 시 받는 이자 </Text>
          <Text style={{...styles.rateCalc, marginBottom:'15%'}} >{(Math.floor(detail.총금리 * Number(money)/100)).toLocaleString('eu-US')} {ISO[ntnCode]}</Text>
        </View>
        <TouchableOpacity style={{...ButtonStyle.button, backgroundColor:money==='0'?'grey':'#293694'}} onPress={onPress} disabled={money === "0"}>
          <Text style={{...ButtonStyle.btnFont, fontSize:RFPercentage(2.5)}}>가입</Text>
        </TouchableOpacity>
      </View> 
    </View>
  )
}

const styles = StyleSheet.create({
  titleContent:{
    width:'100%',
    color:'#123C8D',
    height:SCREEN_HEIGHT*0.2,
    marginBottom:SCREEN_HEIGHT*0.15
  },
  rateBg:{
    backgroundColor:'#B9C2FF',
    marginVertical:'2%',
    borderRadius:10,
    paddingHorizontal:'3%',
    paddingVertical:'3%',
    color:'#123C8D',
    alignSelf: 'flex-start'
  },
  middleContainer:{
    width:'95%',
    height:SCREEN_HEIGHT*0.2,
    justifyContent:'center',
    alignItems:'center',
  },
  inputContainer:{
    borderRadius:15,
    borderBlockColor: Background.background.backgroundColor,
    backgroundColor:'white',
    height:'70%',
    width:'100%',
    paddingHorizontal:'10%',
    marginBottom:SCREEN_HEIGHT*0.08
  },
  txtInput:{
    height:'40%',
    fontSize:RFPercentage(2.5),
    paddingHorizontal:'10%',
    color:'grey'
  },
  nationSel:{
    width:'100%',
    height:'35%',
    marginTop:'10%',
    backgroundColor:'white',
    borderRadius: 10,
    borderBottomColor: Background.background.backgroundColor,
    borderBottomWidth: StyleSheet.hairlineWidth*5,
  },
  nowRate:{
    fontSize:RFPercentage(3),
    fontWeight:'bold',
    marginBottom:SCREEN_HEIGHT*0.02,
  },
  plusInfo:{
    fontWeight:'bold',
  },
  rateCalc:{
    fontWeight:'bold',
    fontSize:RFPercentage(3),
  },

})