import { Box, Button, FormControl, InputLabel, MenuItem, Modal, Select, TextField } from '@mui/material'
import { DATA_USER, DATA_ZONE } from '../../redux/selectors/selectors';
import { GET_ALL_USER, REGISTER_USER } from '../../redux/api/service/userService';
import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux';
import { validateExistsEmail, validateExistsPhone } from '../../utils/ValidationForm';

import { GET_ALL_ZONE } from '../../redux/api/service/zoneService';
import { formatDate } from '../../utils/FormatDate';
import { formatText } from '../../utils/FormatText';

function RegisterForm({ open, handleClose, role, handleReload }) {

  const users = useSelector(DATA_USER);
  const zone = useSelector(DATA_ZONE);
  const dispatch = useDispatch();

  const [zoneId, setZoneId] = useState("");
  const handleChangeZoneId = (e) => {
    setZoneId(e.target.value);
  }

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

  const handleAddAccount = (e) => {
    e.preventDefault();

    // validate email and phone
    if (validateExistsEmail(users, e.target.email.value)) {
      if (validateExistsPhone(users, e.target.phone.value)) {
        setErrorEmail("Email is exists");
        setErrorPhone("Phone is exists");
        return;
      } else {
        setErrorEmail("Email is exists");
        setErrorPhone("");
        return;
      }
    } else {
      if (validateExistsPhone(users, e.target.phone.value)) {
        setErrorPhone("Phone is exists");
        setErrorEmail("");
        return;
      }
    }

    const result = {
      firstName: formatText(e.target.firstName.value),
      lastName: formatText(e.target.lastName.value),
      email: formatText(e.target.email.value),
      password: formatText(e.target.password.value),
      phone: formatText(e.target.phone.value),
      address: formatText(e.target.address.value),
      zoneId: zoneId,
      sex: sex,
      dateOfBirth: formatDate(e.target.dateOfBirth.value),
      roles: role,
      status: true
    }
    console.log("result: ", result);
    dispatch(REGISTER_USER(result));
    setTimeout(() => {
      handleReload();
    }, 200)
    handleClose();
  }

  const handleCloseForm = () => {
    setErrorEmail("");
    setErrorPhone("");
    handleClose();
  }

  useEffect(() => {
    setErrorEmail("");
    setErrorPhone("");
    dispatch(GET_ALL_USER());
    dispatch(GET_ALL_ZONE())
  }, [])

  return (
    <Modal
      open={open}
      onClose={handleCloseForm}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={style}>
        <form action="" onSubmit={handleAddAccount} encType="multipart/form-data">
          <TextField id="filled-basic" label="FirstName" name='firstName' variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          <TextField id="filled-basic" label="LastName" name='lastName' variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          <TextField error={errorEmail} id="filled-basic" label={errorEmail ? errorEmail : "Email"} name='email' variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          <TextField id="filled-basic" label="Password" name='password' variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          <TextField error={errorPhone} id="filled-basic" label={errorPhone ? errorPhone : "Phone"} name='phone' variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          <TextField id="filled-basic" label="Address" name='address' variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          <div style={{ display: 'flex', width: '100%', gap: '10px', margin: '5px 0' }}>
            <FormControl required sx={{ width: '50%' }}>
              <InputLabel id="demo-simple-select-label">Sex</InputLabel>
              <Select labelId="demo-simple-select-label" id="demo-simple-select" value={sex} label="Storage" onChange={handleChangeSex} required>
                <MenuItem value={true}>Nam</MenuItem>
                <MenuItem value={false}>Nữ</MenuItem>
              </Select>
            </FormControl>
            <FormControl sx={{ width: '50%' }}>
              <InputLabel id="demo-simple-select-label">Zone</InputLabel>
              <Select labelId="demo-simple-select-label" id="demo-simple-select" multiline value={zoneId} label="Category" onChange={handleChangeZoneId} required>
                {zone && zone.map(item => (<MenuItem key={item.id} value={item.id}>{item.zoneName}</MenuItem>))}
              </Select>
            </FormControl>
          </div>
          <TextField label="Ngày sinh" color="secondary" focused name="dateOfBirth" type="date" sx={{ width: '100%', margin: "10px 0" }} required />
          <Button type='submit' sx={{ mt: 2, width: "100%" }} variant="contained"> THÊM </Button>
        </form>
      </Box>
    </Modal>
  )
}

export default RegisterForm