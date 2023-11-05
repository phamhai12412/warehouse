import { CHANGE_STATUS_USER, GET_ALL_USER } from "../../redux/api/service/userService";
import React, { useEffect, useState } from "react";
import { TIME_OUT, debouncing } from "../../utils/Deboucing";
import { useDispatch, useSelector } from "react-redux";

import Button from '@mui/material/Button';
import { DATA_USER } from "../../redux/selectors/selectors";
import EditIcon from '@mui/icons-material/Edit';
import FileUploadIcon from '@mui/icons-material/FileUpload';
import FormUpdateUser from "../../components/form/FormUpdateUser";
import LockOpenOutlinedIcon from '@mui/icons-material/LockOpenOutlined';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Paper from '@mui/material/Paper';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import RegisterForm from "../../components/login_register/RegisterForm";
import SettingsIcon from '@mui/icons-material/Settings';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import TextField from '@mui/material/TextField';
import { Tooltip } from "@mui/material";
import { exportExcel } from "../../utils/ExportExcel";

function AccountMarket() {
  const users = useSelector(DATA_USER);
  const [toggleAdd, setToggleAdd] = useState(false);
  const dispatch = useDispatch();
  const [reload, setReload] = useState(false);
  const handleReload = () => setReload(!reload);

  const [open, setOpen] = useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const [openEdit, setOpenEdit] = useState(false);
  const handleOpenEdit = () => setOpenEdit(true);
  const handleClodeEdit = () => setOpenEdit(false);

  const [edit, setEdit] = useState(null);
  const handleEdit = (id) => {
    for (let i = 0; i < users.length; i++) {
      if (users[i].id === id) {
        setEdit(users[i]);
        break;
      }
    }
    handleOpenEdit();
  }

  const handleChangeStatusUser = (id) => {
    dispatch(CHANGE_STATUS_USER(id));
    setTimeout(() => {
      handleReload();
    }, 300)
  }

  const [search, setSearch] = useState('');
  const handleChangeSearch = (e) => setSearch(e.target.value);

  const handleExportExcel = () => {
    exportExcel('/api/v1/users/export/smk', 'user_market');
  }

  useEffect(() => {
    dispatch(GET_ALL_USER(search));
  }, [toggleAdd, reload, search])

  return (
    <div>
      <div className='flex justify-center text-3xl font-semibold uppercase'>quản lý tài khoản siêu thị</div>
      <div className='flex justify-between'>
        <div className="actions">
          <Button variant="contained" className='flex gap-2' onClick={handleExportExcel}> <FileUploadIcon /> <span>Xuất File Excel</span> </Button>
        </div>
        <div className="add_manager">
          <Button variant="contained" className='flex gap-2' onClick={handleOpen}> <PersonAddIcon /> <span>Thêm Tài Khoản</span> </Button>
          <RegisterForm
            open={open}
            handleClose={handleClose}
            role={"ROLE_SUPERMARKET"}
            handleReload={handleReload}
          />
          {edit && <FormUpdateUser
            openEdit={openEdit}
            handleCloseEdit={handleClodeEdit}
            edit={edit}
            handleReload={handleReload}
          />}
        </div>
      </div>
      <div className="content  w-full mt-5 ">
        <div className="header bg-white p-6 shadow-md">
          <TextField onChange={debouncing(handleChangeSearch, TIME_OUT)} id="outlined-basic" size='small' fullWidth label="Tìm kiếm theo email nhân viên viên siêu thị" name='search' variant="outlined" />
        </div>
        <div className="table w-full mt-5 shadow-md">
          <TableContainer component={Paper} >
            <Table sx={{ width: '100%' }} aria-label="simple table">
              <TableHead>
                <TableRow>
                  <TableCell align="center" sx={{ width: '70px' }}>STT</TableCell>
                  <TableCell align="left" sx={{ width: '300px' }}>HỌ TÊN</TableCell>
                  <TableCell align="left" sx={{ width: '300px' }}>EMAIL</TableCell>
                  <TableCell align="center">SỐ ĐIỆN THOẠI</TableCell>
                  <TableCell align="center">ĐỊA CHỈ</TableCell>
                  <TableCell align="center">SIÊU THỊ QUẢN LÝ</TableCell>
                  <TableCell align='center'>GIỚI TÍNH</TableCell>
                  <TableCell align='center'>NGÀY SINH</TableCell>
                  <TableCell align='center'>TRẠNG THÁI</TableCell>
                  <TableCell align='center' sx={{ width: '300px' }}>HÀNH ĐỘNG</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {users && users.map((item, index) => {
                  return (item?.roles === "ROLE_SUPERMARKET" ?
                    <TableRow
                      key={item.id}
                      sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                    >
                      <TableCell align='center'>{index + 1}</TableCell>
                      <TableCell align='left' style={{ width: "300px" }}> {item.firstName} {item.lastName}  </TableCell>
                      <TableCell align="left">{item.email}</TableCell>
                      <TableCell align="center">{item.phone}</TableCell>
                      <TableCell align="center">{item.address.toUpperCase()}</TableCell>
                      <TableCell align="center">{item.storageName.toUpperCase()}</TableCell>
                      <TableCell align="center">{item.sex ? "Nam" : "Nữ"}</TableCell>
                      <TableCell align='center'>{item.dateOfBirth}</TableCell>
                      <TableCell align='center'>{item.status ? <i className="fa-solid fa-lock-open"></i> : <i className="fa-solid fa-lock"></i>}</TableCell>
                      <TableCell align='center'>
                        {item.status ?
                          <div className="flex gap-2 justify-center">
                            <Button onClick={() => handleEdit(item.id)} variant="contained" color="warning"><Tooltip title='edit'><EditIcon /></Tooltip></Button>
                            <Button onClick={() => handleChangeStatusUser(item.id)} variant="contained" color="error"><Tooltip title='lock'><LockOutlinedIcon /></Tooltip></Button>
                          </div>
                          :
                          <Button onClick={() => handleChangeStatusUser(item.id)} variant="contained" color="success"><Tooltip title='unlock'><LockOpenOutlinedIcon /></Tooltip></Button>
                        }
                      </TableCell>
                    </TableRow> : ''
                  )
                })}
              </TableBody>
            </Table>
          </TableContainer>
        </div>
      </div>
    </div >
  );
}

export default AccountMarket;
