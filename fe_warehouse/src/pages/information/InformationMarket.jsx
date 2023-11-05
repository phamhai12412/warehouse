import { Button, FormControl, InputLabel, MenuItem, Select, TextField } from '@mui/material';
import { Cookies, useCookies } from 'react-cookie';
import React, { useEffect, useState } from 'react'

import { UPDATE_USER } from '../../redux/api/service/userService';
import { formatDate } from '../../utils/FormatDate';
import { formatText } from '../../utils/FormatText';
import instance from '../../redux/api';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';

function InformationMarket() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user')));

  const [sex, setSex] = useState(true);
  const handleChangeSex = (e) => {
    setSex(e.target.value)
  }

  const [cookie, setCookie] = useCookies();

  const handleUpdateInfo = async (e) => {
    e.preventDefault();

    const result = {
      id: user.id,
      firstName: formatText(e.target.firstName.value),
      lastName: formatText(e.target.lastName.value),
      email: formatText(e.target.email.value),
      phone: formatText(e.target.phone.value),
      address: formatText(e.target.address.value),
      sex: sex,
      dateOfBirth: formatDate(e.target.dateOfBirth.value)
    };
    console.log(result)
    await instance.put(`/api/v1/users/update_user/${user.id}`, result, {
      headers: {
        Authorization: `Bearer ${new Cookies().get("token")}`
      }
    })
      .then(resp => {
        setCookie("fullName", formatFullName(resp.data.firstName + " " + resp.data.lastName), { path: '/', maxAge: 86400000 })
        localStorage.setItem('user', JSON.stringify({ ...user, ...result }))
        navigate('/market')
      })
      .catch((err) => console.log(err));
  }

  const formatFullName = (text) => {
    let arr = text.split(" ");
    let newArr = [];
    for (let i = 0; i < arr.length; i++) {
      newArr.push(arr[i].charAt(0).toUpperCase() + arr[i].slice(1));
    }
    return newArr.join(" ");
  }

  useEffect(() => {
    if (!user) {
      navigate("/");
    }
  }, [])


  return (
    <div className='flex justify-center'>
      <div className='bg-white w-fit p-5'>
        {user ? <form action="" onSubmit={handleUpdateInfo}>
          <div className='w-96 py-2'><TextField fullWidth name='firstName' defaultValue={user.firstName} id="outlined-basic" label="First Name" variant="outlined" /></div>
          <div className='w-96 py-2'><TextField fullWidth name='lastName' defaultValue={user.lastName} id="outlined-basic" label="Last Name" variant="outlined" /></div>
          <div className='w-96 py-2'><TextField fullWidth name='email' defaultValue={user.username} InputProps={{ readOnly: true }} id="outlined-basic" label="Email" variant="outlined" /></div>
          <div className='w-96 py-2'><TextField fullWidth name='address' defaultValue={user.address} id="outlined-basic" label="Address" variant="outlined" /></div>
          <div className='w-96 py-2'><TextField fullWidth name='phone' defaultValue={user.phone} id="outlined-basic" label="Phone" variant="outlined" /></div>
          <div className='flex items-center gap-3'>
            <FormControl required sx={{ width: '50%' }}>
              <InputLabel id="demo-simple-select-label">Sex</InputLabel>
              <Select labelId="demo-simple-select-label" id="demo-simple-select" defaultValue={user.sex} label="Storage" onChange={handleChangeSex} required>
                <MenuItem value={true}>Nam</MenuItem>
                <MenuItem value={false}>Nữ</MenuItem>
              </Select>
            </FormControl>
            <TextField label="Ngày sinh" color="secondary" focused name="dateOfBirth" defaultValue={user.dateOfBirth} type="date" sx={{ width: '50%', margin: "10px 0" }} required />
          </div>
          <Button type='submit' sx={{ mt: 2, width: "100%" }} variant="contained"> cập nhật </Button>
        </form> : navigate('/')}
      </div>
    </div>

  )
}

export default InformationMarket