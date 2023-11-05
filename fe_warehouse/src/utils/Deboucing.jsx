export const debouncing = (callback, delay) => {

  let timeout = null;

  return (...args) => {
    clearTimeout(timeout);
    timeout = setTimeout(() => {
      callback(...args)
    }, delay);
  }
}

export const TIME_OUT = 500; 