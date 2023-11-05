import { ADD_SHIP, CHANGE_STATUS_SHIP, GET_ALL_SHIP, UPDATE_SHIP } from '../../redux/api/service/shipService';
import { Button, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Tooltip } from '@mui/material';
import React, { useEffect, useState } from 'react'
import { TIME_OUT, debouncing } from '../../utils/Deboucing';
import { useDispatch, useSelector } from 'react-redux';
import { validateBlank, validateExistsShipName, validateExistsShipNameUpdate, validateNumber } from '../../utils/ValidationForm';

import { DATA_SHIP } from '../../redux/selectors/selectors';
import EditIcon from '@mui/icons-material/Edit';
import FileUploadIcon from '@mui/icons-material/FileUpload';
import FormEditShip from '../../components/form/FormEditShip';
import FormShipper from '../../components/form/FormShipper';
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import LockOpenOutlinedIcon from '@mui/icons-material/LockOpenOutlined';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import SettingsIcon from '@mui/icons-material/Settings';
import { formatText } from '../../utils/FormatText';

function Shipper() {

  const ship = useSelector(DATA_SHIP);
  const dispatch = useDispatch();

  const [toggleAdd, setToggleAdd] = useState(false);
  const [result, setResult] = useState({ shipName: "", price: 0, status: true });

  const [reload, setReload] = useState(false);
  const handleReload = () => setReload(!reload);

  const [open, setOpen] = useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const [edit, setEdit] = useState({});
  const handleEditShip = (id) => {
    for (let i = 0; i < ship.length; i++) {
      if (ship[i].id === id) {
        setEdit(ship[i]);
        break;
      }
    }
    handleOpen();
    setErrorShipName('');
    setErrorPrice('');
  }

  const [errorShipName, setErrorShipName] = useState("");
  const [errorPrice, setErrorPrice] = useState("");

  const handleUpdateShip = (e) => {
    e.preventDefault();

    // validate blank
    if (validateBlank(e.target.shipName.value)) {
      if (validateBlank(e.target.price.value)) {
        setErrorShipName("Không được để trống");
        setErrorPrice("Không được để trống");
        return;
      } else {
        setErrorShipName("Không được để trống");
        setErrorPrice("");
        return;
      }
    } else {
      if (validateBlank(e.target.price.value)) {
        setErrorPrice("Không được để trống");
        setErrorShipName("");
        return;
      }
    }

    // validate ship name
    if (validateExistsShipNameUpdate(ship, e.target.shipName.value, edit.shipName)) {
      setErrorShipName("ShipName is exists");
      return;
    }

    // validate price number
    if (validateNumber(e.target.price.value)) {
      setErrorPrice("Giá tiền phải lơn hơn 0");
      setErrorShipName("");
      return;
    }


    const result = {
      shipName: formatText(e.target.shipName.value),
      price: e.target.price.value,
      status: true
    };

    dispatch(UPDATE_SHIP({ data: result, id: e.target.id.value }));
    setTimeout(() => {
      handleReload();
    }, 200)
    handleClose();
  }

  const handleCreateForm = () => {
    setToggleAdd(true);
    setErrorShipName('');
    setErrorPrice('');
  }

  const handleChangeStatusShip = (id) => {
    dispatch(CHANGE_STATUS_SHIP(id));
    setTimeout(() => {
      handleReload();
    }, 200)
  }

  const handleChangeForm = (e) => {
    const { name, value } = e.target;
    setResult({ ...result, [name]: value });
  }


  const handleAddShipper = () => {
    // validate blank
    if (validateBlank(result.shipName)) {
      if (validateBlank(result.price)) {
        setErrorShipName("Không được để trống");
        setErrorPrice("Không được để trống");
        return;
      } else {
        setErrorShipName("Không được để trống");
        setErrorPrice("");
        return;
      }
    } else {
      if (validateBlank(result.price)) {
        setErrorPrice("Không được để trống");
        setErrorShipName("");
        return;
      }
    }

    // validate ship name
    if (validateExistsShipName(ship, result.shipName)) {
      setErrorShipName("Tên đơn vị vận chuyển đã bị trùng");
      return;
    }

    // validate price number
    if (validateNumber(result.price)) {
      setErrorPrice("Giá tiền phải lơn hơn 0");
      setErrorShipName("");
      return;
    }

    dispatch(ADD_SHIP({ shipName: formatText(result.shipName), price: result.price, status: true }));
    setToggleAdd(false);
    setResult({ shipName: "", price: 0, status: true });
  }

  const handleCloseForm = () => {
    setErrorShipName("");
    setErrorPrice("");
    setResult({ shipName: "", price: 0, status: true })
    setToggleAdd(false);
  }

  const [search, setSearch] = useState('');
  const handleChangeSearch = (e) => setSearch(e.target.value);

  useEffect(() => {
    dispatch(GET_ALL_SHIP(search));
  }, [reload, search])

  return (
    <div>
      <div className='flex justify-center text-3xl font-semibold uppercase'>quản lý đơn vị vận chuyển</div>
      <div className='flex flex-col gap-5 '>
        <div className='flex justify-between'>
          <div className="actions">
          </div>
          <div className="add_manager">
            <Button variant="contained" className='flex gap-2' onClick={handleCreateForm}> <LocalShippingIcon /> <span>Thêm Đơn Vị Vận Chuyển</span> </Button>
            {edit && <FormEditShip
              open={open}
              handleClose={handleClose}
              edit={edit}
              handleUpdateShip={handleUpdateShip}
              errorShipName={errorShipName}
              errorPrice={errorPrice}
            />}
          </div>
        </div>
        <div className="header bg-white p-6 shadow-md">
          <TextField onChange={debouncing(handleChangeSearch, TIME_OUT)} id="outlined-basic" size='small' fullWidth label="Tìm kiếm tên đơn vị vận chuyển" name='search' variant="outlined" />
        </div>
      </div>

      <div className="content  w-full mt-5 ">

        <div className="table w-full mt-5 shadow-md">
          <TableContainer component={Paper} >
            <Table sx={{ width: '100%' }} aria-label="simple table">
              <TableHead>
                <TableRow>
                  <TableCell align="center">STT</TableCell>
                  <TableCell align="center">TÊN ĐƠN VỊ</TableCell>
                  <TableCell align='center'>GIÁ THÀNH</TableCell>
                  <TableCell align='center'>TRẠNG THÁI</TableCell>
                  <TableCell align='center'>HÀNH ĐỘNG</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {toggleAdd && <FormShipper
                  result={result}
                  handleCloseForm={handleCloseForm}
                  handleChangeForm={handleChangeForm}
                  handleAddShipper={handleAddShipper}
                  errorShipName={errorShipName}
                  errorPrice={errorPrice}
                />}
                {ship && ship.map((item, index) => {
                  return (
                    <TableRow
                      key={item?.id}
                      sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                    >
                      <TableCell align='center'>{index + 1}</TableCell>
                      <TableCell align='center'>{item?.shipName.toUpperCase()} </TableCell>
                      <TableCell align='center'>{item?.price.toLocaleString()} đ / kg</TableCell>
                      <TableCell align='center'>{item?.status ? <i className="fa-solid fa-lock-open"></i> : <i className="fa-solid fa-lock"></i>}</TableCell>
                      <TableCell align='center'>
                        {item?.status ?
                          <div className='flex gap-2 justify-center'>
                            <Button onClick={() => handleEditShip(item?.id)} variant="contained" color='warning'>
                              <Tooltip title='edit'><EditIcon /></Tooltip>
                            </Button>
                            <Button onClick={() => handleChangeStatusShip(item?.id)} variant="contained" color='error'>
                              <Tooltip title='lock'><LockOutlinedIcon /></Tooltip>
                            </Button>
                          </div>
                          :
                          <Button onClick={() => handleChangeStatusShip(item?.id)} variant="contained" color='success'>
                            <Tooltip title='unlock'><LockOpenOutlinedIcon /></Tooltip>
                          </Button>
                        }
                      </TableCell>
                    </TableRow>
                  )
                })}
              </TableBody>
            </Table>
          </TableContainer>
        </div>
      </div>
    </div>
  )
}

export default Shipper