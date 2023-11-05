import { Box, Button, Modal, TextField } from '@mui/material'
import React, { useState } from 'react'

import { Cookies } from 'react-cookie';
import Swal from 'sweetalert2';
import { formatText } from '../../utils/FormatText';
import instance from '../../redux/api';
import { validateBlank } from '../../utils/ValidationForm';

function FormChangePassword({ open, handleClose, handleLogout }) {

  const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
  };

  const [errorOldPassword, setErrorOldPassword] = useState('')
  const [errorNewPassword, setErrorNewPassword] = useState('')

  const handleSubmit = (e) => {
    e.preventDefault();

    if (validateBlank(formatText(e.target.oldPassword.value))) {
      if (validateBlank(formatText(e.target.newPassword.value))) {
        setErrorOldPassword("Không dược để trống");
        setErrorNewPassword("Không được để trống");
        return;
      } else {
        setErrorOldPassword("Không dược để trống");
        return
      }
    } else {
      if (validateBlank(formatText(e.target.newPassword.value))) {
        setErrorNewPassword("Không được để trống");
        return;
      }
    }
    if (formatText(e.target.newPassword.value) !== formatText(e.target.confirmPassword.value)) {
      setErrorNewPassword("Không khẩu không trùng nhau");
      return;
    }
    instance.put('/api/v1/users/edit-password',
      {
        passwordUser: e.target.oldPassword.value,
        passwordUserEdit: e.target.newPassword.value
      },
      {
        headers: {
          Authorization: `Bearer ${new Cookies().get('token')}`
        }
      })
      .then(resp => console.log(resp.data))
      .catch(err => console.log(err))
    handleClose();
    Swal.fire({
      title: 'Good job!',
      text: 'Thay đổi mật khẩu thành công!',
      icon: 'success',
      showCloseButton: true,
      cancelButtonColor: '#27ae60',
      cancelButtonText: 'OK',
    }).then((result) => {
      handleLogout();
    })
  }

  return (
    <Modal
      open={open}
      onClose={handleClose}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={style}>
        <h1 className='text-center py-5 text-xl'>Change Password</h1>
        <form action='' className='flex flex-col gap-5' onSubmit={handleSubmit}>
          <TextField
            error={errorOldPassword}
            fullWidth id="outlined-basic"
            label={errorOldPassword ? errorOldPassword : "Old Password"}
            name='oldPassword' type='password'
            variant="outlined"
          />
          <TextField
            error={errorNewPassword}
            fullWidth id="outlined-basic"
            label={errorNewPassword ? errorNewPassword : "New Password"}
            name='newPassword' type='password'
            variant="outlined"
          />
          <TextField
            error={errorNewPassword}
            fullWidth id="outlined-basic"
            label={errorNewPassword ? errorNewPassword : "Confirm Password"}
            name='confirmPassword' type='password'
            variant="outlined"
          />
          <Button type='submit' variant="contained">UPDATE</Button>
        </form>
      </Box>
    </Modal>
  )
}

export default FormChangePassword