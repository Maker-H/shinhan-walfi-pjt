import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from  '../../common/http-common';


import flagKRW from '../../assets/flag/KRW.png';
import flagUSD from '../../assets/flag/USD.png';
import flagEUR from '../../assets/flag/EUR.png';
import flagCNY from '../../assets/flag/CNY.png';
import flagJPY from '../../assets/flag/JPY.png';
import flagAUD from '../../assets/flag/AUD.png';

const flagImage = {
  'KRW': flagKRW,
  'USD': flagUSD,
  'EUR': flagEUR,
  'JPY': flagJPY,
  'CNY': flagCNY,
  'AUD': flagAUD 
}

const ISO = {
  'KRW': '원',
  'USD': '$',
  'EUR': '€',
  'JPY': '¥',
  'CNY': '¥',
  'AUD': 'AU$' 
}



export const makeAccount = createAsyncThunk('POST_MAKEACCOUJNT', async(data, {rejectWithValue})=>{
  try{
    const response = await axios.post('product',data);
  }catch(err){
    console.log(err)
    return rejectWithValue(err);
  }
})

// 환율 정보 불러오기
export const getExchangeRate = createAsyncThunk('GET_EXCHANGE_RATE', async(_,{ rejectWithValue })=>{
  try{
    const response = await axios.get('exchange/info')
    const exchangeDtoList = response.data.data.exchangeDtoList;
    const exchanges = {} 
    exchangeDtoList.map((exchange)=>{
      exchange['ISO'] = ISO[exchange.통화코드];
      exchanges[exchange.통화코드] = exchange
    })
    return exchanges
  }catch(err){
    return rejectWithValue(err.response) 
  }
});

// 처음 로그인 해서 계좌 불러오는 action

// 처음 불러온 카드 추가 로직 
export const getAccounts = createAsyncThunk('GET_ACCOUNT', async (data, { rejectWithValue }) => {
  try {
    const response = await axios.post(`account?userId=${data.userId}&userMainAccount=${data.mainAccount}`,{
    })
    const accountDtoList = response.data.data.accountDtoList;
      const accounts = accountDtoList.map((account, index)=>{
        const data = {
          accId: index,
          accountnum: account.계좌번호,
          ntnCode:account.통화, 
          balance: account.잔액통화별,
          cardType: account.상품명, // '저축예금' || '정기적금'
          image: flagImage[account.통화],
          ISO: ISO[account.통화]
        }
        return data
      })
    return accounts
  } catch (err) {
    console.log(err)
    return rejectWithValue(err.response);
  }
});

//이체 axios

// export const postExchange = createAsyncThunk('POST_EXCHANGE', async())
export const postSendMoney = createAsyncThunk('POST_SENDMONEY', async(data, { rejectWithValue }) => {
  try {
    const response = await axios.post('bank/transfer', data)
    return response
  } catch (err) {
    console.log('지갑Slice.postSendMoney',err.response)
    return rejectWithValue(err.response);
  }
})

export const postExchangeKRW = createAsyncThunk('POST_EXCHANGEKRW', async(data, { rejectWithValue }) => {
  try {
    const response = await axios.post('exchange/toglobal', data)
    return response
  } catch (err) {
    console.log('지갑Slice.EXCHANGEMONEY',err.response)
    return rejectWithValue(err.response);
  }
})

export const postExchangeFOR = createAsyncThunk('POST_EXCHANGEFOR', async(data, { rejectWithValue }) => {
  try {
    const response = await axios.post('exchange/fromglobal', data)
    return response
  } catch (err) {
    console.log('지갑Slice.EXCHANGEMONEY',err.response)
    return rejectWithValue(err.response);
  }
})

const initialState = {
  cards:null, // 월렛 들어갈 때 카드 컴포넌트 받아오면서 저장.
  exchangeRates: null,
}


export const walletSlice = createSlice({
  name: 'wallet',
  initialState,
  reducers:{
    minusMoney(state, action){
      // console.log(action);
      const data = action.payload
      state.cards[data.outAccId].balance -= data.num_money
    },
    exchangeMoney(state, action){
      const {exchangedMoney, num_money, outAccId, toNation} = action.payload;
      let toId = 0;
      for(let card of state.cards){
        if(card.ntnCode===toNation && card.cardType==='저축예금'){
          toId=card.accId;
          break;
        }
      }
      if(toNation!=='KRW'){
        state.cards[toId].balance += Number(num_money)
        state.cards[outAccId].balance -= Number(exchangedMoney)
      }else{
        state.cards[toId].balance += Number(exchangedMoney)
        state.cards[outAccId].balance -= Number(num_money)
      }
      // 내 통장, ntnCode 받아서 ntnCode에 해당하는 통장 id 찾기. 
      // 내 통장에서 금액 차감하고 ntnCode 해당 통장에는 넣어야 한다. 
      // 여기서 환율? 을 뽑아내야함. 
    },
    checkAcc(state, action){

      // 해당 Id에 해당하는 통장의 내역을 보여줌. 
    }
    // 카드 추가, 돈 추가, 빼는 로직 
  },
  extraReducers: (builder)=>{
    builder.
    addCase(getExchangeRate.fulfilled, (state, {payload})=>{
      state.exchangeRates = payload;
    })
    .addCase(getAccounts.fulfilled, (state,{payload}) => {
      state.cards = payload
    })
  }
})


export const { minusMoney, exchangeMoney } = walletSlice.actions
export default walletSlice.reducer