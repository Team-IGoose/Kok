import { styled } from '@mui/material/styles';
import { Box, TextField, Button } from '@mui/material';

// 컨테이너 스타일
export const Container = styled(Box)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'center',
  alignItems: 'center',
  height: '2rem',
  width: '100%',
  padding: theme.spacing(2),
}));

// 입력 필드 스타일
export const InputField = styled(TextField)(({ theme }) => ({
  marginBottom: theme.spacing(2),
  width: '80%',
}));

// 시작하기 버튼 스타일
export const StartButton = styled(Button)(({ theme }) => ({
  marginTop: theme.spacing(2),
  width: '80%',
  borderRadius: '1rem',
  color: '#ffffff',
  backgroundColor: '#9C9CFF', // 배경색 설정
}));
