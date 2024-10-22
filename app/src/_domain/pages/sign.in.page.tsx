import React from 'react';
import { Typography } from '@mui/material';
import * as S from "./style";

const SignInPage: React.FC = () => {

  const handleSignIn = () => {
    console.log('클릭했삼~');
  };

  return (
    <S.Container>
      <Typography variant="h4" gutterBottom>
        테스트
      </Typography>
      <S.InputField
        label="전화번호 또는 이메일"
        variant="outlined"
        margin="normal"
        fullWidth
      />
      <S.StartButton
        variant="contained"
        color="primary"
        onClick={handleSignIn}
      >
        시작하기
      </S.StartButton>
    </S.Container>
  );
};

export default SignInPage;
