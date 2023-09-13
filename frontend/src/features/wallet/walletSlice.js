import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";
// 환율 정보 불러오기
export const getExchangeRate = createAsyncThunk('GET_EXCHANGE_RATE', async(_,{ rejectWithValue })=>{
  try{
    const response = await axios.get('http://j9d101a.p.ssafy.io:8094/exchange/info',{
      headers:{
        'Content-Type': 'application/json'
      }
    })
    const exchangeDtoList = response.data.data.exchangeDtoList;
    return exchangeDtoList 
  }catch(err){
    return rejectWithValue(err.response) 
  }
});


// 처음 로그인 해서 계좌 불러오는 action

// 처음 불러온 카드 추가 로직 
export const getAccount = createAsyncThunk('GET_ACCOUNT', async (_, { rejectWithValue }) => {
  try {
    const response = await axios.post('http://j9d101a.p.ssafy.io:8094/account?userId=ssafy',{
      headers:{
        'Content-Type': 'application/json'
      }
    })
    const accountDtoList = response.data.data.accountDtoList;
      const accounts = accountDtoList.map((account, index)=>{
        const data = {
          accId: index,
          accountnum: account.계좌번호,
          ntnCode:account.통화, 
          balance: account.잔액통화별,
          cardType: account.상품명
        }
        return data
      })
    return accounts
  } catch (err) {
    console.log(err)
    return rejectWithValue(err.response);
  }
});


const initialState = {
  cards:null, // 월렛 들어갈 때 카드 컴포넌트 받아오면서 저장.
  exchangeRates: null,
}


export const walletSlice = createSlice({
  name: 'wallet',
  initialState,
  reducers:{

    
    // 카드 추가, 돈 추가, 빼는 로직 
  },
  extraReducers: (builder)=>{
    builder.
    addCase(getExchangeRate.fulfilled, (state, {payload})=>{
      state.exchangeRates = payload;
    })
    .addCase(getAccount.fulfilled, (state,{payload}) => {
      state.cards = payload
    })
  }
})


export const {} = walletSlice.actions
export default walletSlice.reducer