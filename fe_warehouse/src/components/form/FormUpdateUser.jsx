import { Box, Button, FormControl, InputLabel, MenuItem, Modal, Select, TextField } from '@mui/material'
import React, { useState } from 'react'
import { useDispatch, useSelector } from 'react-redux';
import { validateExistsEmail, validateExistsEmailUpdate, validateExistsPhone, validateExistsPhoneUpdate } from '../../utils/ValidationForm';

import { DATA_USER } from '../../redux/selectors/selectors';
import { UPDATE_USER } from '../../redux/api/service/userService';
import { formatDate } from '../../utils/FormatDate';
import { formatText } from '../../utils/FormatText';

function FormUpdateUser({ openEdit, handleCloseEdit, edit, handleReload }) {

  const dispatch = useDispatch();
  const users = useSelector(DATA_USER);

  const [sex, setSex] = useState(true);
  const handleChangeSex = (e) => {
    setSex(e.target.value)
  }

  const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 500,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
  };

  const [errorEmail, setErrorEmail] = useState("");
  const [errorPhone, setErrorPhone] = useState("");

  const handleUpdateUser = (e) => {
    e.preventDefault();

    // validate email and phone
    if (validateExistsEmailUpdate(users, e.target.email.value, edit.email)) {
      if (validateExistsPhoneUpdate(users, e.target.phone.value, edit.phone)) {
        setErrorEmail("Email is exists");
        setErrorPhone("Phone is exists");
        return;
      } else {
        setErrorEmail("Email is exists");
        setErrorPhone("");
        return;
      }
    } else {
      if (validateExistsPhoneUpdate(users, e.target.phone.value, edit.phone)) {
        setErrorPhone("Phone is exists");
        setErrorEmail("");
        return;
      }
    }

    const result = {
      id: e.target.id.value,
      firstName: formatText(e.target.firstName.value),
      lastName: formatText(e.target.lastName.value),
      email: formatText(e.target.email.value),
      // password: formatText(e.target.address.value),
      phone: formatText(e.target.phone.value),
      address: formatText(e.target.address.value),
      sex: sex,
      dateOfBirth: formatDate(e.target.dateOfBirth.value)
    };
    dispatch(UPDATE_USER({ data: result, id: e.target.id.value }));
    setTimeout(() => {
      handleReload();
      handleCloseEdit()
    }, 200)
  }

  const handleCloseForm = () => {
    setErrorEmail("");
    setErrorPhone("");
    handleCloseEdit();
  }

  return (
    <Modal
      open={openEdit}
      onClose={handleCloseForm}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={style}>
        <form action="" onSubmit={handleUpdateUser} encType="multipart/form-data">
          <TextField id="filled-basic" label="ID" defaultValue={edit.id} name='id' InputProps={{ readOnly: true }} variant="filled" sx={{ margin: '5px 0', display: 'none' }} fullWidth required />
          <TextField id="filled-basic" label="FirstName" defaultValue={edit.firstName} name='firstName' variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          <TextField id="filled-basic" label="LastName" defaultValue={edit.lastName} name='lastName' variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          <TextField error={errorEmail} id="filled-basic" label={errorEmail ? errorEmail : "Email"} InputProps={{ readOnly: true }} defaultValue={edit.email} name='email' variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          <TextField error={errorPhone} id="filled-basic" label={errorPhone ? errorPhone : "Phone"} defaultValue={edit.phone} name='phone' variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          <TextField id="filled-basic" label="Address" defaultValue={edit.address} name='address' variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          <div className='flex gap-2 items-center'>
            <FormControl sx={{ width: '50%' }}>
              <InputLabel id="demo-simple-select-label">Giới tính</InputLabel>
              <Select labelId="demo-simple-select-label" id="demo-simple-select" defaultValue={edit.sex} label="Storage" onChange={handleChangeSex} required>
                <MenuItem value={true}>Nam</MenuItem>
                <MenuItem value={false}>Nữ</MenuItem>
              </Select>
            </FormControl>
            <TextField label="Ngày sinh" color="secondary" defaultValue={edit.dateOfBirth} focused name="dateOfBirth" type="date" sx={{ width: '50%', margin: "10px 0" }} required />
          </div>
          <Button type='submit' sx={{ mt: 2, width: "100%" }} variant="contained"> CẬP NHẬT </Button>
        </form>
      </Box>
    </Modal>
  )
}

export default FormUpdateUser