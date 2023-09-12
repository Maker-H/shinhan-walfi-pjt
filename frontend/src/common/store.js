import {configureStore} from '@reduxjs/toolkit';

import walletReducer from '../features/wallet/walletSlice'
import authReducer from '../features/auth/authSlice';

export const store = configureStore({
  reducer:{
    wallet: walletReducer,
    auth: authReducer
  }
});
